package com.kevinthomasbradley.incidentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main entry point for the Spring Boot application.
// The @SpringBootApplication annotation enables component scanning, auto-configuration, and configuration properties.
@SpringBootApplication
public class Application {

    // The main method starts the Spring Boot application.
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

