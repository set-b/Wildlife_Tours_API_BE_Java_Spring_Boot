package com.example.ecommerce.config;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_TOURS;
import static com.example.ecommerce.constants.StringConstants.MANAGER_ROLE_TYPE;

import com.example.ecommerce.security.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] AUTH_WHITELIST = {
      "/swagger-ui/index.html",
      "/webjars/**",
      "/v2/api-docs",
      "swagger-resources/**"
  };

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public WebMvcConfigurer corsConfigurer() { // this codeblock breaks everything because of a circular reference of dependencies
//    return new WebMvcConfigurer() {
//      @Override
//      public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedMethods("GET", "POST", "PUT", "DELETE")
//            .allowedOrigins("http://localhost:3000", "http://localhost:8080");
//      }
//    };
//  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(new AuthFilter(), AuthFilter.class)
        .authorizeRequests()
        .antMatchers(AUTH_WHITELIST).permitAll()
        .antMatchers(HttpMethod.GET).permitAll()
        .antMatchers(HttpMethod.DELETE, CONTEXT_TOURS).hasAuthority(MANAGER_ROLE_TYPE)
        .antMatchers(HttpMethod.PUT,CONTEXT_TOURS).hasAuthority(MANAGER_ROLE_TYPE)
        .antMatchers(HttpMethod.POST,CONTEXT_TOURS).hasAuthority(MANAGER_ROLE_TYPE)
        .and()
        .sessionManagement().disable()
        .csrf().disable();
  }

  @Override
  public void configure(WebSecurity web){
    web.ignoring().antMatchers(HttpMethod.OPTIONS);
    web.ignoring().antMatchers("/swagger-ui/index.html", "/login");
  }
}
