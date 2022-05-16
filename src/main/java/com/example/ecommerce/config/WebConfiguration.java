package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  //@Value("${application.myapp.in-browser-allowed-origins}")
  private final String[] inBrowserAllowedOrigins = new String[] {"http://localhost:3000", "http://localhost:8080"};

  private final String[] inBrowserAllowedMethods = new String[]{"GET", "PUT", "POST", "DELETE"};

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/in-browser/login")
        .allowedOrigins(inBrowserAllowedOrigins)
        .allowedMethods(inBrowserAllowedMethods);
    registry.addMapping("/**").allowedOrigins(); //might need to change allowed origins in the future
  }
}
