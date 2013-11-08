package it.uninsubria.dista.anonymizedshare.controllers;

import it.uninsubria.dista.anonymizedshare.exceptions.CreationParameterNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.LoginNotValidException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullCreationException;
import it.uninsubria.dista.anonymizedshare.exceptions.NullParameterException;
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
		String password = httpServletRequest.getParameter("password");
		
		try {
			if(name!=null && surname!=null && email!=null && password!=null) {
				SocialUser tempUser = new SocialUser();
				tempUser.setName(name);
				tempUser.setSurname(surname);
				tempUser.setEmail(email);
				tempUser.setPassword(password);
				SocialUser user = socialUserService.create(tempUser);
				uiModel.addAttribute("user", user);
			} else {
				throw new NullParameterException();
			}
		} catch (NullParameterException npe) {
			String errore = "Non posso creare l'utente. Mancanano parametri necessari";
			uiModel.addAttribute("errorMsg", errore);
			npe.printStackTrace();
		} catch (NullCreationException nce) {
			String errore = "C'è stato un errore durante la creazione dell'utente";
			uiModel.addAttribute("errorMsg", errore);
			nce.printStackTrace();
		} catch (LoginNotValidException lnve) {
			String errore = "Esiste già un utente con questa email registrata";
			uiModel.addAttribute("errorMsg", errore);
			lnve.printStackTrace();
		} 
		
		return "create";
	}
}
