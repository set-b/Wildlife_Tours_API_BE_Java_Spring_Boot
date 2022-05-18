package com.example.ecommerce.models;

import static com.example.ecommerce.constants.StringConstants.REQUIRED_FIELD;

import com.example.ecommerce.validators.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * This Address embeddable contains all information and properties about an Address
 */
@Embeddable
@Table(name = "contacts")
public class Contact {

  // accept all non-special characters
  private String firstName;
  // accept all non-special characters
  private String lastName;
  // phone number regex
  private String phoneNo;
  // email annotation
  private String email;

  public Contact(){}

  public Contact(String firstName, String lastName, String phoneNo, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNo = phoneNo;
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", phoneNo='" + phoneNo + '\'' +
        ", email='" + email + '\'' +
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
    Contact contact = (Contact) o;
    return Objects.equals(firstName, contact.firstName) && Objects.equals(
        lastName, contact.lastName) && Objects.equals(phoneNo, contact.phoneNo)
        && Objects.equals(email, contact.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, phoneNo, email);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return
        Objects.isNull(firstName) &&
            Objects.isNull(lastName) &&
            Objects.isNull(phoneNo) &&
            Objects.isNull(email);
  }
}
