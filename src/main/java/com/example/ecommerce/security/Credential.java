package com.example.ecommerce.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class Credential {

  @Email
  @NotNull
  private String email;

  @Length(min = 8)
  @NotNull
  private String password;

  public Credential() {
  }

  public Credential(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
