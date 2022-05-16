package com.example.ecommerce.exceptions;

import java.util.Date;
import java.util.List;

/**
 * This class contains custom exception info to be incorporated in the Exception Controller.
 */
public class ValidationExceptionResponse extends ExceptionResponse {

  public List<String> validationErrors;

  public ValidationExceptionResponse(String errorType, Date date, String errorMessage,
      List<String> errors) {
    super(errorType, date, errorMessage);
    validationErrors = errors;
  }

}
