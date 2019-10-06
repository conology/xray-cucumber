package net.conology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.conology.config.ConnectionProperties;

@SpringBootApplication
public class HITApplication {
	
	@Autowired
	private ConnectionProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(HITApplication.class, args);

	}

}
