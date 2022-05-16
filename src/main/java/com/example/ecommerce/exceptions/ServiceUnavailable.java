package com.example.ecommerce.exceptions;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class ServiceUnavailable extends RuntimeException {

  public ServiceUnavailable() {

  }

  public ServiceUnavailable(String message) {
    super(message);
  }

  public ServiceUnavailable(Exception e) {
    super(e.getCause());
  }
}
