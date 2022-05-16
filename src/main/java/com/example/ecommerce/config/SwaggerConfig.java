package com.example.ecommerce.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * This class configures the swagger documentation for the project, which can also be used for
 * manual testing of the endpoints in the controller
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .pathMapping("/").enableUrlTemplating(true)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiDetails());
  }

  private ApiInfo apiDetails() {
    return new ApiInfo(
        "Ecommerce REST API",
        "Java REST API project for an ecommerce website",
        "1.0",
        "Sample",
        new springfox.documentation.service.Contact("Brandyn Tse",
            "https://www.linkedin.com/in/brandyn-tse-085872166",
            "brandyntse941@gmail.com"),
        "n/a",
        "n/a",
        Collections.emptyList());
  }
}
