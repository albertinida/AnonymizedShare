package it.uninsubria.dista.anonymizedshare.controllers;

import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/broadcast")
public class BroadcastController {
	
	@Value("${crypto.key.public}")
	private String pubKey;
	
	@Value("${crypto.km.key}")
	private String keyManagerKey;
	
	@ResponseBody
	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String broadcastPublicKeys(HttpServletRequest httpServletRequest) {
		
		/*
		 * prendere il parametro id della richiesta
		 *  
		 * mandare una richiesta al KeyManager per ottenere la chiave pubblica
		 * 
		 * ricevere la risposta
		 * 
		 * controllare l'id della risposta
		 * 
		 * (se id corretto) aggiungere alla risposta la propria chiave pubblica
		 * 
		 * return della risposta
		 * 
		 * (se id non corretto) return messaggio di errore
		 * 
		 */
		
		//estrae i parametri dalla richiesta
		int seed = Integer.parseInt(httpServletRequest.getParameter("seed"));
		String message = httpServletRequest.getParameter("message");
		JSONObject json = new JSONObject();
		try {
			//costruisce la stringa in formato json da inviare al key manager
			json.put("seed",seed);	//valore random
			json.put("message", message);	//messaggio che indica al KM la richiesta di key-broadcast
			//costruisce la richiesta http - url , POST , content-type e content-length
			URL url = new URL("http://localhost:8888/KeyManager/");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", Integer.toString(json.toString().getBytes().length));
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			//spedisce la stringa e attende la risposta dal KM
			outputStream.writeBytes(json.toString());
			outputStream.flush();
			String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = reader.readLine()) != null) {
		    	response.append(line);
		    	response.append('\r');
		    }
		    //risposta dal KeyManager : costruisce un nuovo oggetto JSON con la stringa ricevuta
		    json = new JSONObject(response.toString());
		    
		    	if(seed - json.getInt("seed") == 1)		//confronta i parametri restituiti dal KM con quelli precedentemente inviati
		    		if(json.getInt("seed") == seed+1) {
		    			keyManagerKey = json.getString("KM-key");	//estrae la chiave pubblica del KM 
		    			json = new JSONObject();		//crea una nuova stringa json da inviare al browser
		    			json.put("seed++", seed+1);		//valore random intero incrementato di 1
		    			json.put("AS-Key", pubKey);	//chiave pubblica 
		    			json.put("seed--",seed-1);		//valore random intero decrementato di 1
		    			json.put("KM-Key",keyManagerKey);	//chiave pubblica del keymanager
		    			return json.toString();
		    	}
		    
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{ \"result\": \"error\" }\n";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/", produces = "text/plain", method = RequestMethod.GET)
	public String broadcastPublicKeysGet() {
		// chiamata ai log di sistema per tracciare una chiamata non valida.
		return null;
	}
	
	/*
	@ResponseBody
	@RequestMapping(value = "/test", produces = "text/html")
	public String testRSAKeys(Model uiModel) throws NoSuchAlgorithmException {
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.generateKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < publicKey.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
        }
        
        String keytest = "\npublic="+retString.toString();

        byte[] privateKey = keyPair.getPrivate().getEncoded();
        retString = new StringBuffer();
        for (int i = 0; i < privateKey.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (privateKey[i] & 0x00FF)).substring(1));
        }
        
        keytest += "\nprivate="+retString.toString();

        
		return keytest;
	}*/
}