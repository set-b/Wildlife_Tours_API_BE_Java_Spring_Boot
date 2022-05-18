package com.example.ecommerce.validators;

import javax.validation.Constraint;

@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {

  String message() default "Phone number must be valid";
}
