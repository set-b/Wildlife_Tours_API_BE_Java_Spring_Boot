package com.example.ecommerce.exceptions;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class BadDataResponse extends RuntimeException {

  public BadDataResponse() {
  }

  public BadDataResponse(String message) {
    super(message);
  }
}
