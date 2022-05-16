package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This launches the Spring Boot Application
 */
@SpringBootApplication
public class WildlifeToursApplication {

  public static void main(String[] args) {
    SpringApplication.run(WildlifeToursApplication.class, args);

  }
}
/*
Todo:
-Create entities and configure bidirectional relationships
-Create data and validators, if necessary
-Create Controllers, Repos, and Services
-Testing
-Possibly add atuthentiation and authorization through Oauth and jwt tokens
 */