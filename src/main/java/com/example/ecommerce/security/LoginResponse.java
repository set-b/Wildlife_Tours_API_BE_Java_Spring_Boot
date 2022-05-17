package com.example.ecommerce.security;

import org.springframework.stereotype.Component;

@Component
public class LoginResponse {

  private String jwt;

  public LoginResponse(){super();} //super why?

  public LoginResponse(String jwt) {
    this.jwt = jwt;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }
}
