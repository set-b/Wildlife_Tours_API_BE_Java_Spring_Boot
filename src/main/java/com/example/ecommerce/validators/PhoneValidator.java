package com.example.ecommerce.validators;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

  PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  @Override
  public void initialize(Phone constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String phoneNo, ConstraintValidatorContext constraintValidatorContext) {
    return phoneUtil.isPossibleNumber(phoneNo, "US");
  }
}
