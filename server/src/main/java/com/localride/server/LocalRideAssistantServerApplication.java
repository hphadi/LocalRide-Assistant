package com.localride.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // import this to scan for components in the specified packages

/**
 * Main application class for the Local Ride Assistant Server.
 * This class is responsible for bootstrapping the Spring Boot application.
 * It scans for components in the specified packages.
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.localride.server", "com.localride.server.LocalRideAssistantServer"})
public class LocalRideAssistantServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalRideAssistantServerApplication.class, args);
	}
}