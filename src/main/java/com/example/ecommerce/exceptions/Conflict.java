package com.example.ecommerce.exceptions;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class Conflict extends RuntimeException {

  public Conflict(String message) {
    super(message);
  }

}
