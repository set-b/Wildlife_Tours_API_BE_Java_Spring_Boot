package com.example.ecommerce.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "user_account")
public class UserAccount {
//todo include validation for fields?? (i.e. notnull, etc.)
  @Id
  @Column(name = "user_account_id") // might change to "user_id"
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String role;
  private boolean isActive;

  public UserAccount() {
    super();
  }

  public UserAccount(String username, String password, boolean isActive, String role) {
    this.username = username;
    this.password = password;
    this.isActive = isActive;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public String getRole(){return role;}

  public void setRole(String role){this.role = role;}
}
