package it.uninsubria.dista.anonymizedshare.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/broadcast")
public class BroadcastController {

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
			json.put("seed",seed);
			json.put("message", message);
			URL url = new URL("http://...");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Length", Integer.toString(json.toString().getBytes().length));
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			outputStream.writeBytes(json.toString());
			outputStream.flush();
			
			String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = reader.readLine()) != null) {
		    	response.append(line);
		    	response.append('\r');
		    }
		    json = new JSONObject(response.toString());
		    String publicKey = null;
		    String keyManagerKey = json.getString("KM-key");
		    if(seed - json.getInt("seed") == 1)
		    	if(json.getInt("seed") == seed+1) {
		    		json = new JSONObject();
		    		json.put("seed++", seed+1);
		    		json.put("AS-Key", publicKey);
		    		json.put("seed--",seed-1);
		    		json.put("KM-Key",keyManagerKey);
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
}