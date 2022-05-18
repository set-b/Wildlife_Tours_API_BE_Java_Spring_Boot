package com.example.ecommerce.exceptions;

public class BadRequest extends RuntimeException {

  //public BadRequest(){};
  public BadRequest(String message) {
    super(message);
  }
}
