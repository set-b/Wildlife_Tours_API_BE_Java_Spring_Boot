package com.example.ecommerce.config;

import static com.example.ecommerce.constants.StringConstants.ADMIN;
import static com.example.ecommerce.constants.StringConstants.BASIC_USER;
import static com.example.ecommerce.constants.StringConstants.CONTEXT_TOURS;
import static com.example.ecommerce.constants.StringConstants.CONTEXT_TOUR_BOOKINGS;
import static com.example.ecommerce.constants.StringConstants.CONTEXT_USER_ACCOUNTS;
import static com.example.ecommerce.constants.StringConstants.EMPLOYEE;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/logout").permitAll()
        .antMatchers(HttpMethod.GET, CONTEXT_TOURS).permitAll()
        .antMatchers(HttpMethod.POST, CONTEXT_USER_ACCOUNTS).permitAll()

        .antMatchers(HttpMethod.POST, CONTEXT_TOUR_BOOKINGS)
        .hasAnyAuthority(BASIC_USER, ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.GET, CONTEXT_TOUR_BOOKINGS + "/tourcode/{tourcode}")
        .hasAnyAuthority(BASIC_USER, ADMIN, EMPLOYEE)

        .antMatchers(HttpMethod.PUT, CONTEXT_TOURS + "/{id}").hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.GET, CONTEXT_USER_ACCOUNTS).hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.GET, CONTEXT_USER_ACCOUNTS + "/{id}")
        .hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.PUT, CONTEXT_USER_ACCOUNTS + "{id}")
        .hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.GET, CONTEXT_TOUR_BOOKINGS).hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.GET, CONTEXT_TOUR_BOOKINGS + "{id}")
        .hasAnyAuthority(ADMIN, EMPLOYEE)
        .antMatchers(HttpMethod.PUT, CONTEXT_TOUR_BOOKINGS + "{id}")
        .hasAnyAuthority(ADMIN, EMPLOYEE)

        .antMatchers(HttpMethod.DELETE, CONTEXT_USER_ACCOUNTS + "{id}").hasAnyAuthority(ADMIN)
        .antMatchers(HttpMethod.DELETE, CONTEXT_TOURS + "/{id}").hasAnyAuthority(ADMIN)
        .antMatchers(HttpMethod.DELETE, CONTEXT_TOUR_BOOKINGS + "{id}").hasAnyAuthority(ADMIN)
        .antMatchers(HttpMethod.POST, CONTEXT_TOURS).hasAnyAuthority(ADMIN)

        .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(
            SessionCreationPolicy.STATELESS);

    http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
