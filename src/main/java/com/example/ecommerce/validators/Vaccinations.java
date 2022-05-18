package com.example.ecommerce.validators;

import javax.validation.Constraint;

@Constraint(validatedBy = VaccinesValidator.class)
public @interface Vaccinations {

  String message() default "Vaccines must be at least 2 characters in length";

}
