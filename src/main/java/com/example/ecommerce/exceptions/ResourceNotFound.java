package com.example.ecommerce.exceptions;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class ResourceNotFound extends RuntimeException {

  public ResourceNotFound() {
  }

  public ResourceNotFound(String message) {
    super(message);
  }
}
