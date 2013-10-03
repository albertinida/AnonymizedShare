package it.uninsubria.dista.anonymizedshare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/download")
public class DownloadController {

	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String downloadResource(HttpServletRequest httpservletRequest) {
		
		/*
		 * 
		 * processare la richiesta, che contiene l'id del file richiesto e anche l'id owner
		 * 
		 * associare alla coppia id owner e id file r-file([0,5])
		 * 
		 * inviare r-file al browser
		 * 
		 * chiamata del tipo downloadService.createNewDownloadRequest(parametri)
		 * 
		 * mandare una richiesta di evaluation al PathFinder
		 * 
		 * aspettare la risposta dal KeyManager contenente l'url della rsc e il secret (secret/random)
		 * 
		 * inoltrare la risposta inserendo il secondo secret-u necessario alla decifratura del file
		 *
		 */
		return null;
	}
	
	
	@RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
	public String downloadResourceGet() {

		// produce pagina html per il download
		
		return null;
	}
	
}
	
