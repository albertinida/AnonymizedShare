package it.uninsubria.dista.anonymizedshare.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import it.uninsubria.dista.anonymizedshare.exceptions.LoginNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullSessionException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;
import it.uninsubria.dista.anonymizedshare.services.UserSessionService;
import javax.crypto.*;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
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
	SocialUserService socialUserService;
	
	@Autowired
	UserSessionService userSessionService;
	
	@ResponseBody
	@RequestMapping(value ="/", produces = "text/html", method = RequestMethod.GET)
	public String loginGet(HttpServletRequest httpServletRequest, Model uiModel) {
		
		// TODO: da controllare e gestire con i cookies.
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("activeSessionId")) {
					
					try {
						SocialUser user = userSessionService.fetchUser(cookie.getValue());
						uiModel.addAttribute("user", user);

					} catch (NullSessionException nse) {
						nse.printStackTrace();
					}
					
				}
			}
		}
		return "login";
	}

	@RequestMapping(value ="/", produces ="text/html", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest httpServletRequest,Model uiModel) {
		
		String email = httpServletRequest.getParameter("email");
		String password = httpServletRequest.getParameter("password");
		
		try {
			
			if (email!=null && password!=null) {
				SocialUser tempUser = new SocialUser();
				tempUser.setEmail(email);
				tempUser.setPassword(password);
				
				SocialUser user = socialUserService.login(tempUser);
				uiModel.addAttribute("sessionId", userSessionService.fetchSession(user).getSessionId());
				uiModel.addAttribute("user", user);

			} else {
				throw new NullParameterException();
			}
		
			
		} catch (LoginNotValidException lnve) {
			String errore = "Utente non trovato";
			uiModel.addAttribute("errorMsg", errore);
			lnve.printStackTrace();
		} catch (NullParameterException npe) {
			String errore = "Non posso fare login. Mancano parametri necessari";
			uiModel.addAttribute("errorMsg", errore);
			npe.printStackTrace();
		} catch (NullSessionException nse) {
			String errore = "Non posso fare login. Ci sono stati errori nel creare la sessione";
			uiModel.addAttribute("errorMsg", errore);
			nse.printStackTrace();
		}
		
		return "login";
	}
	
	@ResponseBody
	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String getAsymmetricKeys(HttpServletRequest httpServletRequest, Model uiModel) {
		/*
		 * prendere il parametro id della richiesta
		 *  
		 * inoltrare la richiesta al KeyManager 
		 * 
		 * ricevere la risposta, contenente i parametri per lo scambio di chiavi diffie-hellman
		 * 
		 * generare la chiave di sessione tramite scambio di firme d-h
		 * 
		 * ri-inoltrare la richiesta al KeyManager tramite chiave di sessione
		 * 
		 * ricevere la risposta
		 * 
		 * (se id corretto) return della risposta contenente le chiavi asimmetriche 
		 * 
		 * inviare al KeyManager mex di avvenuta ricezione
		 * 
		 * (se id non corretto) return messaggio di errore
		 * 
		 */
		
		//estrae i parametri dalla richiesta in ingresso
		BigInteger uid = new BigInteger(httpServletRequest.getParameter("id"));  
		int random = Integer.parseInt(httpServletRequest.getParameter("seed"));  
		String message = httpServletRequest.getParameter("message"); 	// message = "Diffie Hellman Key";
		URL url;
	    HttpURLConnection connection;  
	    JSONObject jsonObject = new JSONObject();
	    try{
		 //oggetto json
			jsonObject.put("id", uid);  		//id 
			jsonObject.put("seed", random); 	//valore random
			jsonObject.put("message", message); //messaggio
			//converte l'oggetto json in un array di bytes per eseguire la cifratura
			byte[] bytes = jsonObject.toString().getBytes();
			//costruzione chiave pubblica RSA del Key Manager
			RSAPublicKeySpec keyManagerPublicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(keyManagerExponent));
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey keyManagerPublicKey = fact.generatePublic(keyManagerPublicKeySpec);
			//esegue la cifratura con algoritmo RSA
			Cipher cipher = Cipher.getInstance("RSA");	
			cipher.init(Cipher.ENCRYPT_MODE, keyManagerPublicKey);
			byte[] cipherText = cipher.doFinal(bytes);
			int length = cipherText.length; //lunghezza in byte del messaggio da inviare
			url = new URL("http://localhost:8888/KeyManager/"); 
			connection = (HttpURLConnection)url.openConnection();
			//parametri dell'header : POST, content-type, content-length
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text");		
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Length", Integer.toString(length));
			//invia il messaggio cifrato
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
			objectOutputStream.write(cipherText);
			objectOutputStream.flush();
			//risposta : id-utente, valore random modificato e parametri per lo scambio D-H cifrati con la chiave privata del KeyManager
			/* KM esegue una procedura di questo tipo per generare i parametri Diffie-Hellman
			*AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
			*paramGen.init(1024);
			*AlgorithmParameters params = paramGen.generateParameters();
			*DHParameterSpec dhSpec = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);
			*return ""+dhSpec.getP()+","+dhSpec.getG()+","+dhSpec.getL();
			*/
			ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());		
			cipherText = (byte[])objectInputStream.readObject();
			uiModel.addAttribute("cipher",cipherText);
			
	    }catch(IOException e){
	    	e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	    	// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "{ \"result\": \"success\" }\n";		
	}
	
	/*
	 * Metodo per il calcolo di una chiave di sessione Diffie - Hellman
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/DiffieHellmanParameters", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String diffieHellmanExchange(HttpServletRequest httpServletRequest, Model uiModel){
		//il messaggio cifrato viene inviato dal client in forma di stringa costruita su bytes cifrati 
		String request = httpServletRequest.getParameter("request"); //riceve (g^a mod p) cifrato
		//la stringa viene convertita in byte[] per poterla decifrare
		byte[] cipherText = request.getBytes();
		try {			
			HttpURLConnection connection;
			URL url = new URL("http://localhost:8888/KeyManager/");
			//costruisce la chiave privata per decifrare
			RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(privateExponent));
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = factory.generatePrivate(privateKeySpec);
			//viene eseguita la decifratura dei bytes con chiave privata AS
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] bytes = cipher.doFinal(cipherText);
			//viene costruito l'oggetto json sulla stringa decifrata per estrarre i parametri
			JSONObject json = new JSONObject(new String(bytes));
			//estrae la parte di messaggio cifrata con la chiave pubblica di KM e la inserisce in un nuovo oggetto json per poterla inviare
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text");		
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Length", Integer.toString(cipherText.length));
			ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
			outputStream.writeObject(cipherText);
			outputStream.flush();
			ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
			byte[] response = (byte[])inputStream.readObject();
			//restituisce al browser il messaggio cifrato
			uiModel.addAttribute("response", response);
			//restituisce un messaggio per confermare l'operazione o segnalere errore
			return "{\"result\" : \"success\"}\n";		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return "{\"result\" : \"error\"}\n";		
	}
	
	@ResponseBody
	@RequestMapping(value = "/userPublicKey", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String getUserPublicKey(HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, JSONException, IOException {
		//riceve la richiesta, formata da una stringa costruita su byte cifrati
		String request = httpServletRequest.getParameter("request");	
		//costruisce la chiave privata
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(privateExponent));
		KeyFactory factory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = factory.generatePrivate(privateKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		//decifra i byte con la chiave privata
		byte[] plainText = cipher.doFinal(request.getBytes());
		//costruisce il json corrispondente
		JSONObject json = new JSONObject(new String(plainText));
		//estrae l'id utente e cerca l'utente con l'id ricevuto
		BigInteger userId = new BigInteger(json.getString("IDu"));
		SocialUser user = socialUserService.getById(userId);
		String userModulus = json.getString("modulus");
		String userExponent = json.getString("exponent");
		user.setModulus(userModulus);
		user.setExponent(userExponent);
		byte[] messageToKeyManager = (byte[]) json.get("cipherMessage");
		URL url = new URL("http://localhost:8888/KeyManager/");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text");		
		connection.setUseCaches (false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Length", Integer.toString(messageToKeyManager.length));
		
		//spedisce a KeyManager la restante parte di messaggio
		ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
		outputStream.write(messageToKeyManager);
		outputStream.flush();
		
		
		return "{\"result\" :  \"success\" }";
	}
	
	
}
