package it.uninsubria.dista.anonymizedshare.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping(value = "/", produces = "text/html")
	public String index(Model uiModel) {

		uiModel.addAttribute("message", "test message");
		return "index";
	}
}
