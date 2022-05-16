package com.example.ecommerce.exceptions;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class UniqueFieldViolation extends RuntimeException {

  public UniqueFieldViolation() {
  }

  public UniqueFieldViolation(String message) {
    super(message);
  }
}
