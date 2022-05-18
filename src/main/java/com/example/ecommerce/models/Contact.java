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

  private String firstName;
  private String lastName;
  private String phoneNo;
  private String email;
}
