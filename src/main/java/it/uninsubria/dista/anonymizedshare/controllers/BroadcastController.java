package it.uninsubria.dista.anonymizedshare.controllers;

import javax.servlet.http.HttpServletRequest;

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
		
		return "{ \"result\": \"success\" }\n";
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/", produces = "text/plain", method = RequestMethod.GET)
	public String broadcastPublicKeysGet() {
		// chiamata ai log di sistema per tracciare una chiamata non valida.
		return null;
	}
}