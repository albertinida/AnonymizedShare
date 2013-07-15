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
	
	@Value("${architecture.pathfinder}")
	private String pathFinderAddress;
	
	@RequestMapping(value = "/", produces = "text/html")
	public String index(Model uiModel) { //throws MalformedURLException, IOException, JSONException {

		try {
			HttpURLConnection connection;
			URL url = new URL(keyManagerAddress);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String jsonResponse = "", jsonLine = "";
			
			while ((jsonLine = br.readLine()) != null) 
			 jsonResponse += jsonLine;
			br.close();
			
			JSONObject response = new JSONObject(jsonResponse);
			uiModel.addAttribute("keymanager", response.get("result"));

			url = new URL(pathFinderAddress);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			jsonResponse = ""; jsonLine = "";
			
			while ((jsonLine = br.readLine()) != null) 
			 jsonResponse += jsonLine;
			br.close();
			
			response = new JSONObject(jsonResponse);
			uiModel.addAttribute("pathfinder", response.get("result"));

			
		} catch (Exception e) {
			uiModel.addAttribute("keymanager", e.getClass().toString()+" exception occurred");
			uiModel.addAttribute("pathfinder", e.getClass().toString()+" exception occurred");
			e.printStackTrace();
		}
		
		return "index";
	}
}
