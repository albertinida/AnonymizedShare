package it.uninsubria.dista.anonymizedshare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/login")
public class LoginController {
	
	@ResponseBody
	@RequestMapping(value ="/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String login(HttpServletRequest httpServletRequest) {
		/*
		 *riceve dal browser l'id dell'utente e la richiesta di login 
		 *e la invia al key manager; attende la risposta contenente i parametri
		 *per lo scambio delle chiavi e li invia al browser  
		 */
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value ="/", produces = "text/html", method = RequestMethod.GET)
	public String login() {
		
		return null;
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
		
		return "{ \"result\": \"success\" }\n";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/", produces = "text/plain", method = RequestMethod.GET)
	public String getAsymmetricKeys() {
		// chiamata ai log di sistema per tracciare una chiamata non valida.
		return null;
	}
}
