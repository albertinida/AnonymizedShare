package it.uninsubria.dista.anonymizedshare.controllers;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.Resource;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.services.ResourceService;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;


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

	@RequestMapping(value = "/{token}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String uploadResource(@PathVariable("token") String token, HttpServletRequest httpServletRequest,Model uiModel) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, JSONException, IOException, ClassNotFoundException, CreationParameterNotValidException {
		
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
		//estrae i metadati del file
		String fileName = httpServletRequest.getParameter("fileName");
		//procedimento per estrarre il mimetype dal file: divide il nome del file usando '.' come separatore, e l'ultima parte è il formato
		String[] str = fileName.split("\\.");
		String mimeType = str[str.length-1];
		long size = Long.parseLong(httpServletRequest.getParameter("size"));
		long resourceId = Long.parseLong(token);
		byte[] cipherText = httpServletRequest.getParameter("request").getBytes();
		//costruisce la chiave privata per fare la prima decifratura
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(privateExponent)); 
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = factory.generatePrivate(privateKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		//esegue la decifratura
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] plainText = cipher.doFinal(cipherText);
		//costruisce l'oggetto Json in cui è contenuta una parte cifrata
		JSONObject json = new JSONObject(new String(plainText));
		BigInteger userId = new BigInteger(json.getString("IDu"));
		//verrà utilizzato per i controlli successivi
		int r = json.getInt("number");
		//ricava la chiave pubblica dell'utente usando modulo ed esponente
		SocialUser user = socialUserService.getById(userId);
		BigInteger userModulus = new BigInteger(user.getModulus());
		BigInteger userExponent = new BigInteger(user.getExponent());
		RSAPublicKeySpec userPublicKeySpec = new RSAPublicKeySpec(userModulus,userExponent);
		factory = KeyFactory.getInstance("RSA");
		PublicKey userPublicKey = factory.generatePublic(userPublicKeySpec);
		//effettua il controllo sul file che sta per essere caricato, per assicurarsi che non esista già
		if(resourceService.exists(resourceId, userId)) {
			JSONObject error = new JSONObject();
			//genera il messaggio di errore in formato json
			error.put("message", "id file bad");
			//cifra il messaggio d'errore da restituire con la chiave pubblica dell'utente			
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE,userPublicKey);
			//cifra il messaggio e lo invia in forma di strina al browser
			cipherText = cipher.doFinal(error.toString().getBytes());
			//restituisce il messaggio di errore "ID file bad", cifrato con chiave pubblica di utente
			return new String(cipherText);
			}
		else {
			
			Resource resource = resourceService.create(user, fileName, mimeType, size);
			//estrae il messaggio indirizzato a KeyManager, che è una stringa json cifrata con la chiave pubblica di KeyManager
			byte[] cipherMessage = (byte[])json.get("cipherMessage");
			URL url = new URL("http://localhost:8888/KeyManager/");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text");		
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Length", Integer.toString(cipherMessage.length));
			ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
			outputStream.writeObject(cipherMessage);
			outputStream.flush();
		
			ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
			byte[] response = (byte[])inputStream.readObject();
			r = r+1;
			String secret = socialUserService.getById(userId).getSecret();
			json = new JSONObject();
			json.put("number", r);
			json.put("userSecret", secret);
			json.put("cipherMessage", response);
			cipher.init(Cipher.ENCRYPT_MODE,userPublicKey);
			response = cipher.doFinal(json.toString().getBytes());
			uiModel.addAttribute("response", response);
			return "{\"result\" : \"success\" }";
		}
	}
	
	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String uploadResource(HttpServletRequest httpServletRequest) {
		
		
		String token = httpServletRequest.getParameter("token");
		String file = httpServletRequest.getParameter("file");
		
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
		
	
		return null;
	}
	
	@RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
	public String uploadResourceGet() {

		// produce pagina html per l'upload
		
		return "upload";
	}
}
