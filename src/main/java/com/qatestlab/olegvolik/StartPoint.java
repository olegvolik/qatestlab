package com.qatestlab.olegvolik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Timer;

/** This class is the entry point of the application.
 * Aside from it defines the application run method
 * it also provides means for additional application
 * configuration. The annotation describes the class
 * as the one can be automatically configured by
 * Spring Boot and enables automatic components
 * scanning for registering them in the Spring
 * application context. The components should be marked
 * with the appropriate annotations.
 * @author Oleg Volik
 */

@SpringBootApplication
public class StartPoint extends SpringBootServletInitializer implements WebMvcConfigurer, CommandLineRunner {

    @Autowired
    private EventChecker eventChecker;

    /** The main method which is invoked first to run
     * the application
     * @param args - command line arguments array
     */
    public static void main(String[] args) {
        SpringApplication.run(StartPoint.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("homepage");
    }

    @Override
    public void run(String... args) throws Exception {
        Timer checkTimer = new Timer(false);
        checkTimer.schedule(eventChecker, 0, 3600000);
    }
}
