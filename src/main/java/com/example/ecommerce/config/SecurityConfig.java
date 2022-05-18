package com.example.ecommerce.config;

import static com.example.ecommerce.constants.StringConstants.ADMIN;

import com.example.ecommerce.security.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  UserDetailsService userDetailsService;
  @Autowired
  AuthorizationFilter authorizationFilter;

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService);
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() { // need to use bcrypt encoder eventually
    return NoOpPasswordEncoder.getInstance();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers(HttpMethod.GET, "/products").hasAuthority(ADMIN)
        //ensure nested authority roles: [{authority: ADMIN}] is checked
        // add method level security to the Tour entities et al. 
        .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(
            SessionCreationPolicy.STATELESS);

    http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
