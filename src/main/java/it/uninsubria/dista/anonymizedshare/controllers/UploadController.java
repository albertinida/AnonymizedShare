package it.uninsubria.dista.anonymizedshare.controllers;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.models.UploadRequest;
import it.uninsubria.dista.anonymizedshare.services.ResourceService;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;
import it.uninsubria.dista.anonymizedshare.services.UploadRequestService;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.Set;
@Controller
@RequestMapping(value = "/upload")
public class UploadController {
	
	@Value("${crypto.modulus}")
	private String modulus;
	
	@Value("${crypto.exponent.public")
	private String publicExponent;
	
	@Value("${crypto.exponent.private}")
	private String privateExponent;
	
	@Value("${keymanager.modulus}")
	private String keyManagerModulus;
	
	@Value("${keymanager.exponent}")
	private String keyManagerExponent;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private SocialUserService socialUserService;
	
	@Autowired 
	private UploadRequestService uploadService;

	@RequestMapping(value = "/{token}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String uploadResource(@PathVariable("token") String token, HttpServletRequest httpServletRequest,Model uiModel) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, JSONException, IOException, ClassNotFoundException, CreationParameterNotValidException, NullParameterException {
		
		/*
		 * 
		 * processare la richiesta, che contiene i metadati del file e anche l'id
		 * 
		 * chiamata del tipo uploadService.createNewUploadRequest(parametri tra cui un identificatore := token)
		 * 
		 * mandare una richiesta al key manager per ottenere un secret
		 * (la richiesta deve contenere l'id della risorsa)
		 * 
		 * ricevere la risposta
		 * 
		 * mandare al browser i due secret
		 *
		 */
		
		//metadati della richiesta di upload
		String uploadRequestId = token;
		String resourceName = httpServletRequest.getParameter("fileName");
		long size = Long.parseLong(httpServletRequest.getParameter("size"));
		//estrae il mimeType dal nome del file
		String[] temp = resourceName.split("\\.");
		String mimeType = temp[temp.length-1];
		
		//richiesta cifrata
		byte[]  cipherText = httpServletRequest.getParameter("cipherText").getBytes();
		
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = factory.generatePrivate(new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(privateExponent)));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE,privateKey);
		//decifratura della richiesta
		byte[] plainText = cipher.doFinal(cipherText);
		JSONObject json = new JSONObject(new String(cipherText));
		BigInteger userId = new BigInteger(json.getString("userId"));
		long resourceId = json.getLong("fileId");
		int number = json.getInt("number");
		
		SocialUser user = socialUserService.getById(userId);
		//controlla che non esistano file con lo stesso id già associati all'utente 
		if(resourceService.exists(resourceId, userId)) {
			JSONObject error = new JSONObject();
			//genera il messaggio di errore in formato json
			error.put("message", "id file bad");
			//cifra il messaggio d'errore da restituire con la chiave pubblica dell'utente
			factory = KeyFactory.getInstance("RSA");
			PublicKey userPublicKey = factory.generatePublic(new RSAPublicKeySpec(new BigInteger(user.getModulus()), new BigInteger(user.getExponent())));
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE,userPublicKey);
			//cifra il messaggio e lo invia in forma di strina al browser
			cipherText = cipher.doFinal(error.toString().getBytes());
			//restituisce il messaggio di errore "ID file bad", cifrato con chiave pubblica di utente
			return new String(cipherText);
			}
		else {
			Resource resource = new Resource();
			resource.setId(resourceId);
			resource.setUserOwner(user);
			resource.setName(resourceName);
			resource.setMimeType(mimeType);
			resource.setSize(size);
			resourceService.create(resource);
			
			UploadRequest uploadRequest = new UploadRequest();
			uploadRequest.setId(new BigInteger(token));
			uploadRequest.setNumber(number);
			uploadRequest.setSocialUser(user);
			uploadService.createNewUploadRequest(uploadRequest);
			
			//viene estratto il sotto-messaggi cifrato da indirizzare a KeyManager
			byte[] messageToKeyManager = (byte[])json.get("cipherMessage");
			URL url = new URL("http://localhost:8888/KeyManager/"); 
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text");		
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Length", Integer.toString(messageToKeyManager.length));
			
			ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
			outputStream.write(messageToKeyManager);
			outputStream.flush();
			
			ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
			byte[] response = (byte[]) inputStream.readObject();
			json = new JSONObject();
			json.put("number", number+1);
			json.put("secret", user.getSecret());
			json.put("cipherMessage", response);
			
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(user.getModulus()), new BigInteger(user.getExponent()));
			factory = KeyFactory.getInstance("RSA");
			PublicKey userPublicKey = factory.generatePublic(publicKeySpec);
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE,userPublicKey);
			response = cipher.doFinal(json.toString().getBytes());
			
			uiModel.addAttribute("response", response);
			return "{\"request\" : \"sent\" }";
		}
	}
	
	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String uploadResource(HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, JSONException, NullParameterException, CreationParameterNotValidException, IOException {
		
		
		String token = httpServletRequest.getParameter("token");
		
		
		/*
		 * -- il file qui è già cifrato
		 * 
		 * retrieve della precedente request
		 * chiamata del tipo UploadRequest ur = uploadService.getUploadRequestByToken(token)
		 * 
		 * invia al key manager il file cifrato con i vari parametri (mimeType, meta dati,...)
		 * presi dall'oggetto 
		 * 
		 * aspetta la risposta
		 * 
		 * se la risposta è ok, return "upload success"
		 * 
		 * se la risposta non lo è, return error data
		 * 
		 */
		
		
		
		UploadRequest uploadRequest = uploadService.getUploadRequest(token);
		
		byte[] request = httpServletRequest.getParameter("request").getBytes();
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = factory.generatePrivate(new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent)));
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE,privateKey);
		byte[] plainText = cipher.doFinal(request);
		JSONObject json = new JSONObject(new String(plainText));
		BigInteger userId = new BigInteger(json.getString("userId"));
		long resourceId = json.getLong("fileId");
		int number = json.getInt("number");
		int sharingDepth = json.getInt("depth");
		
		if(userId.equals(uploadRequest.getSocialUser().getUid())) 
			if(number - uploadRequest.getNumber() == 2) {
				SocialUser user = socialUserService.getById(userId);
				Resource resource = resourceService.getResource(resourceId);
				resourceService.setSharingDepth(resource, sharingDepth);
				resourceService.setModificationDate(resource, new Date());
			}
		
		byte[] messageToKeyManager = (byte[]) json.get("cipherMessage");
		URL url = new URL("http://localhost:8888/KeyManager/"); 
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text");		
		connection.setUseCaches (false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Length", Integer.toString(messageToKeyManager.length));
		
		ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
		outputStream.write(messageToKeyManager);
		outputStream.flush();
		
		return "{\"result\" : \"upload ok\"}";
	}
	
	@RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
	public String uploadResourceGet() {

		// produce pagina html per l'upload
		
		return "upload";
	}
}
