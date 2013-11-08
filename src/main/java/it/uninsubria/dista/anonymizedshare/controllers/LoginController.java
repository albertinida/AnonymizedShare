package it.uninsubria.dista.anonymizedshare.controllers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import it.uninsubria.dista.anonymizedshare.exceptions.LoginNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullSessionException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;
import it.uninsubria.dista.anonymizedshare.services.UserSessionService;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

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
	public String getAsymmetricKeys(HttpServletRequest httpServletRequest) {
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
		//estraggo i parametri dalla richiesta in ingresso
		int id = Integer.parseInt(httpServletRequest.getParameter("id"));       
		int random = Integer.parseInt(httpServletRequest.getParameter("seed"));  
		String message = httpServletRequest.getParameter("message"); 	// message = "Register || new_key";
		URL url;
	    HttpURLConnection connection;  
	    JSONObject jsonObject = new JSONObject();
	    
		try { //oggetto json
			jsonObject.put("id", id);  //id 
			jsonObject.put("seed", random); //valore random
			jsonObject.put("message", message); //messaggio
			int length = (jsonObject.toString()).getBytes().length; //lunghezza in byte del messaggio da inviare
			//parametri per la richiesta
			url = new URL("http://localhost"); 
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");		//parametri dell'header
			connection.setRequestProperty("Content-Length", Integer.toString(length));
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			//invio la richiesta
			DataOutputStream outputStream = new DataOutputStream (connection.getOutputStream ());
			outputStream.writeBytes(jsonObject.toString());
			outputStream.flush();
			
			//risposta
			InputStream inputStream = connection.getInputStream();		
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = bufferedReader.readLine()) != null) {
		    	response.append(line);
		    	response.append('\r');
		    }
		    bufferedReader.close();
		    /*riceve la risposta come stringa in formato json
		     * costruisce l'oggetto JSON a partire dalla stringa ricevuta
		     */
		    JSONObject jsonResponse = new JSONObject(response.toString());
		    if(jsonResponse.getInt("seed")-jsonObject.getInt("seed")==1)
		    	if(jsonResponse.getInt("seed")==random + 1){
		    		/*
				     * controlla che il valore pseudo-random ricevuto sia uguale
				     * all'incremento del primo
				     */
		    		//SCAMBIO DI DIFFIE HELLMAN
		    		int p = jsonResponse.getInt("p");	//estrae i parametri p,g dalla precedente risposta
		    		int g = jsonResponse.getInt("g");
		    		int a = ((int)Math.random()*10);	//determina un valore intero casuale, da usare come esponente
		    		int n = (int)Math.pow(g, a) % p;	// calcola g^a mod p
		    		jsonObject = new JSONObject();
		    		jsonObject.put("msg",n);
		    		length = (jsonObject.toString()).getBytes().length;
		    		connection.setRequestProperty("Content-Length", Integer.toString(length));
		    		connection.setRequestProperty("Content-Type", "application/json");
		    		outputStream.writeBytes(jsonObject.toString());  //invia al key manager il valore (g^a) mod p, scritto in formato json
		    		outputStream.flush();
		    		
		    		//risposta 
		    		
				    while((line = bufferedReader.readLine()) != null) {		//riceve la risposta dal key manager (g^b mod p) come stringa 
				    	response.append(line);							
				    	response.append('\r');							
				    }
				    
				    //costruisce l'oggetto JSON con la stringa ricevuta
				    jsonResponse = new JSONObject(response.toString());
				    int m = jsonResponse.getInt("msg");	//g ^b %p
				    String sessionKey = Integer.toString((int)Math.pow(m,a) % p);
		    	}
		int r = ((int)Math.random())*10;
		jsonObject = new JSONObject();
		jsonObject.put("id", id);	
		jsonObject.put("seed",r);
		jsonObject.put("message","R2R");
		length = jsonObject.toString().length();
		//invia la richiesta per ottenere le chiavi asimmetriche;
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(length));
		outputStream.writeBytes(jsonObject.toString());
		outputStream.flush();
		while((line = bufferedReader.readLine()) != null) {
			response.append(line);							
	    	response.append('\r');
		}
		jsonResponse = new JSONObject(response.toString());
		if(jsonResponse.getInt("id") == id){
			if(jsonResponse.getInt("seed")==random-r) 
				jsonObject = new JSONObject();
				jsonObject.put("seed",jsonResponse.getInt("seed")+1);
				jsonObject.put("message", "THANKS");
				outputStream.writeBytes(jsonObject.toString());
				outputStream.flush();
				return jsonResponse.toString();
			}
		else
			return "{\"result\" : \"error\"}\n";
					
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{ \"result\": \"success\" }\n";
	}
}
