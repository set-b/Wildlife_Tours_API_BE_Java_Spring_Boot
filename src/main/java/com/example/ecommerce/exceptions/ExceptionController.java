package com.example.ecommerce.exceptions;

import static com.example.ecommerce.constants.StringConstants.BAD_DATA;
import static com.example.ecommerce.constants.StringConstants.BAD_REQUEST;
import static com.example.ecommerce.constants.StringConstants.CONFLICT;
import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;
import static com.example.ecommerce.constants.StringConstants.SERVER_ERROR;
import static com.example.ecommerce.constants.StringConstants.UNEXPECTED_ERROR;
import static com.example.ecommerce.constants.StringConstants.VALIDATION_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This controller is used for intercepting and routing exceptions across the entire application.
 */
@ControllerAdvice
public class ExceptionController {

  private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();
    String method = ex.getParameter().getMethod().getName();
    String controller = ex.getParameter().getDeclaringClass().getSimpleName();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String errorMessage = error.getDefaultMessage();
      errors.add(errorMessage);
    });

    ValidationExceptionResponse response = new ValidationExceptionResponse(VALIDATION_ERROR,
        new Date(), "One or more validation errors occurred in:" + controller + " : " + method,
        errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFound.class)
  protected ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFound exception) {
    ExceptionResponse response = new ExceptionResponse(NOT_FOUND, new Date(),
        exception.getMessage());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadDataResponse.class)
  protected ResponseEntity<ExceptionResponse> badDataResponse(BadDataResponse exception) {
    ExceptionResponse response = new ExceptionResponse(BAD_DATA, new Date(),
        exception.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UniqueFieldViolation.class)
  protected ResponseEntity<ExceptionResponse> uniqueFieldViolation(UniqueFieldViolation exception) {
    ExceptionResponse response = new ExceptionResponse(BAD_DATA, new Date(),
        exception.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Conflict.class)
  protected ResponseEntity<ExceptionResponse> conflict(Conflict exception) {
    ExceptionResponse response = new ExceptionResponse(CONFLICT, new Date(),
        exception.getMessage());

    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BadRequest.class)
  protected ResponseEntity<ExceptionResponse> BadRequest(BadRequest exception) {
    ExceptionResponse response = new ExceptionResponse(BAD_REQUEST, new Date(),
        exception.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ServiceUnavailable.class)
  protected ResponseEntity<ExceptionResponse> serverError(ServiceUnavailable ex) {

    String exceptionMessage = ex.getMessage();
    ExceptionResponse response;

    if (ex.getCause() instanceof JDBCException) {
      exceptionMessage = ((JDBCException) ex.getCause()).getSQLException().getMessage();

      response = new ExceptionResponse(SERVER_ERROR, new Date(), exceptionMessage);
      logger.error(exceptionMessage);
      return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    } else {
      exceptionMessage = "Error: " + exceptionMessage
          + " Class: " + ex.getStackTrace()[0].getClassName()
          + " Method: " + ex.getStackTrace()[0].getMethodName()
          + " Line: " + ex.getStackTrace()[0].getLineNumber();
      response = new ExceptionResponse(UNEXPECTED_ERROR, new Date(), exceptionMessage);
      logger.error(exceptionMessage);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
