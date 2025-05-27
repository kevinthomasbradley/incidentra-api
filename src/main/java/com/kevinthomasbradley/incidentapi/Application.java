package com.kevinthomasbradley.incidentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Main entry point for the Incidentra Spring Boot application.
 * <p>
 * The {@code @SpringBootApplication} annotation is a convenience annotation that adds all of the following:
 * <ul>
 *   <li>{@code @Configuration}: Tags the class as a source of bean definitions for the application context.</li>
 *   <li>{@code @EnableAutoConfiguration}: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.</li>
 *   <li>{@code @ComponentScan}: Tells Spring to look for other components, configurations, and services in the {@code com.kevinthomasbradley.incidentapi} package, allowing it to find controllers and other classes.</li>
 * </ul>
 * <p>
 * The {@code main} method uses {@link SpringApplication#run(Class, String...)} to launch the application.
 */
@SpringBootApplication
public class Application {

    /**
     * The main method that serves as the entry point for the Spring Boot application.
     * It delegates to Spring Boot's {@link SpringApplication#run(Class, String...)} method to bootstrap the application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}