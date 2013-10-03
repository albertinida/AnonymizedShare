package it.uninsubria.dista.anonymizedshare.controllers;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.models.SocialUser;
import it.uninsubria.dista.anonymizedshare.services.SocialUserService;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/create")
public class CreateController {

	@Autowired
	SocialUserService socialUserService;
	
	@RequestMapping(produces = "text/html", method = RequestMethod.GET)
	public String renderCreationPage() {
		
		return "create";
	}
	
	@RequestMapping(produces = "text/html", method = RequestMethod.POST)
	public String createUser(HttpServletRequest httpServletRequest, Model uiModel) {
		
		String name = httpServletRequest.getParameter("name");
		String surname = httpServletRequest.getParameter("surname");
		String email = httpServletRequest.getParameter("email");
		
		// TODO: gestire correttamente il passaggio di dati della data di nascita
		// Long day = Long.parseLong(httpServletRequest.getParameter("day"));
		// Long month = Long.parseLong(httpServletRequest.getParameter("month"));
		// Long year = Long.parseLong(httpServletRequest.getParameter("year"));
		Calendar birthday = Calendar.getInstance();

		// TODO: controllare che qui i campi siano diversi da null --per non fare chiamate inutili al service
		
		try {
			SocialUser user = socialUserService.create(name, surname, birthday, email);

			uiModel.addAttribute("user", user);

		
		} catch (CreationParameterNotValidException e) {
			// TODO: gestire le eccezioni (e.g. stampare messaggio di errore)
			
			String errore = "Non posso creare l'utente";
			uiModel.addAttribute("errorMsg", errore);
			e.printStackTrace();
		}
		
		
		return "create";
	}
	
	
}
