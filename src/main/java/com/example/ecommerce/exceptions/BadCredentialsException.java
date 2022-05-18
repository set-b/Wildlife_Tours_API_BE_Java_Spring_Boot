package com.example.ecommerce.exceptions;

public class BadCredentialsException extends RuntimeException {

  public BadCredentialsException() {
  }

  public BadCredentialsException(String message) {
    super(message);
  }
}
