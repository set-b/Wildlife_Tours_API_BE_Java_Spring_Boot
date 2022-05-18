package com.example.ecommerce.models;

import static com.example.ecommerce.constants.StringConstants.BAD_DATA;
import static com.example.ecommerce.constants.StringConstants.REQUIRED_FIELD;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * This User Entity contains all information and properties about a User Object.
 */
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "name " + REQUIRED_FIELD)
  @Column(name = "user_name")
  private String name;

  @NotBlank(message = "title " + REQUIRED_FIELD)
  private String title;

  @NotNull(message = "roles " + REQUIRED_FIELD) //todo: change to single role
  private String role;

  @NotBlank(message = "email " + REQUIRED_FIELD)
  @Email
  @Column(unique = true)
  private String email; // this will be new username

  @NotBlank(message = "password " + REQUIRED_FIELD)
  @Length(min = 8, message = BAD_DATA + "password must be at least 8 characters in length")
  private String password;

  public User() {
  }

  public User(String name, String title,
      @NotNull(message = "roles" + REQUIRED_FIELD) String role,
      String email, String password) {
    this.name = name;
    this.title = title;
    this.role = role;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRoles() {
    return role;
  }

  public void setRoles(String role) {
    this.role = role;
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

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", title='" + title + '\'' +
        ", roles=" + role +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(name, user.name)
        && Objects.equals(title, user.title) && Objects.equals(role, user.role)
        && Objects.equals(email, user.email) && Objects.equals(password,
        user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, title, role, email, password);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return
        Objects.isNull(id) &&
            Objects.isNull(name) &&
            Objects.isNull(title) &&
            Objects.isNull(role) &&
            Objects.isNull(email) &&
            Objects.isNull(password);
  }
}
