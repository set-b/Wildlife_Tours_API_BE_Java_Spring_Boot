package com.example.ecommerce.validators;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VaccinesValidator implements ConstraintValidator<Vaccinations, String[]> {

  @Override
  public void initialize(Vaccinations constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
    if(strings.length == 0){ // empty vaccinations list means no vaccines required
      return true;
    }
    return Arrays.stream(strings).allMatch(l -> l.length() >= 2); // check if all are greater than two or not
  }
  // might change this to a string array of vaccine names, possibly
}
