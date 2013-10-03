package it.uninsubria.dista.anonymizedshare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


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

}
