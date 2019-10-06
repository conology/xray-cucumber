package net.conology.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.conology.config.ConnectionProperties;
import net.conology.connector.XrayServerConnector;

@RestController
@RequestMapping("/api/server")
public class JiraServerController {
	
	@Autowired
	ConnectionProperties properties;
	
	@PostMapping("/{id}")
	public void startExecution(@PathVariable String id)  {
		
		XrayServerConnector con = new XrayServerConnector(properties.getJira().getServer().getUser(),
				properties.getJira().getServer().getPass(),
				properties.getJira().getServer().getUrl());
		
		// read Xray
		con.getGherkinFromTestCloud(id);
		
		// execute test with cucumber
		try {
			cucumber.api.cli.Main.main(defaultOptions);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// public results to Xray
		con.publishResults();
		
	}
	
	
	private static String[] defaultOptions = {
            "--glue", "stepdefs",
            "--plugin", "pretty",
            "--plugin", "json:target/report/cucumber.json",
            "--monochrome",
            "src/test/features"
	};

}
