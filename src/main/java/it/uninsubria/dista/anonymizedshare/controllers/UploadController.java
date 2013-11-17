package it.uninsubria.dista.anonymizedshare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;
@Controller
@RequestMapping(value = "/upload")
public class UploadController {

	@RequestMapping(value = "/{token}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public String uploadResource(@PathVariable("token") String token) {
		
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
		return null;
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
