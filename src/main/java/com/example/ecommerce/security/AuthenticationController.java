package com.example.ecommerce.security;

import static com.example.ecommerce.constants.StringConstants.BAD_CREDENTIALS;

import com.example.ecommerce.config.MyAppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  MyAppUserDetailsService userDetailsService;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping
  public LoginResponse login(@RequestBody LoginRequest loginRequest) { // throws exception?
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
              loginRequest.getPassword()));
    } catch (BadCredentialsException e) {
      throw new com.example.ecommerce.exceptions.BadCredentialsException(BAD_CREDENTIALS);
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

    String jwt = jwtUtils.generateToken(userDetails);

    return new LoginResponse(jwt);
  }
}
