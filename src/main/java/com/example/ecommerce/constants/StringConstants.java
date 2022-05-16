package com.example.ecommerce.constants;

/**
 * This class contains constants used for mapping endpoints and relaying error messages. They are
 * used in the Controllers and Service Implementation classes.
 */
public class StringConstants {

  public static final String REQUIRED_FIELD = "is a required field";
  public static final String QUERY_REQUEST = "Query request received for ";
  public static final String NOT_FOUND = "Could not locate resource: ";
  public static final String VALIDATION_ERROR = "Validation error";
  public static final String UPDATE_REQUEST = "Put request received for ";
  public static final String DELETE_REQUEST = "Delete request received for ";
  public static final String POST_REQUEST = "Post request received for ";
  public static final String BAD_DATA = "Bad data";
  public static final String BAD_REQUEST = "Bad request";
  public static final String SERVER_ERROR = "Server error";
  public static final String UNEXPECTED_ERROR = "Unexpected server error";

  public static final String CONFLICT = "Conflict";

  //endpoint constants
  public static final String CONTEXT_GREETINGS = "/greetings";
  public static final String CONTEXT_CUSTOMERS = "/customers";
  public static final String CONTEXT_ORDERS = "/orders";
  public static final String CONTEXT_PRODUCTS = "/products";
  public static final String CONTEXT_USERS = "/users";

  //roles
  public static final String EMPLOYEE = "employee";
  public static final String ADMIN = "admin";

  //authorization
  public static final String UNAUTHORIZED = "unauthorized";
  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String ISSUER = "wildlife-tour";
  public static final String INVALID_EMAIL_PASSWORD = "Invalid email or password";
  public static final String SECRET_KEY = "secret";
  public static final String CLAIMS_ATTRIBUTE = "claims";
  public static final String EMAIL_ATTRIBUTE = "email";
  public static final String ROLES_ATTRIBUTE = "roles";
  public static final String MANAGER_ROLE_TYPE = "manager";
  public static final String EMPLOYEE_ROLE_TYPE = "employee";
  public static final String MISSING_INVALID_ERROR = "Missing or invalid authorization header";
  public static final String APPLICATION_JSON = "application/json";

}
