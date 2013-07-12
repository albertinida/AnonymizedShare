package it.uninsubria.dista.anonymizedshare.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@Value("${architecture.keymanager}")
	private String keyManagerAddress;
	
	@RequestMapping(value = "/", produces = "text/html")
	public String index(Model uiModel) throws MalformedURLException, IOException, JSONException {

		HttpURLConnection keyManagerConnection;
		URL url = new URL(keyManagerAddress);
		keyManagerConnection = (HttpURLConnection) url.openConnection();
		keyManagerConnection.setRequestMethod("POST");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(keyManagerConnection.getInputStream()));
		String jsonResponse = "", jsonLine = "";
		
		while ((jsonLine = br.readLine()) != null) 
		 jsonResponse += jsonLine;
		br.close();
		
		JSONObject response = new JSONObject(jsonResponse);
		
		uiModel.addAttribute("message", response.get("result"));
		return "index";
	}
}
