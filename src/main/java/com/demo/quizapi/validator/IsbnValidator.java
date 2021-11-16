package com.demo.quizapi.validator;


import com.demo.quizapi.util.HelperClass;
import com.demo.quizapi.util.annotation.Isbn;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IsbnValidator implements ConstraintValidator<Isbn, String> {

  @Override
  public void initialize(Isbn constraintAnnotation) {}

  @Override
  public boolean isValid(String isbn, ConstraintValidatorContext context) {
    log.info("isbn -> {}", isbn);
    return HelperClass.isValidISBN(isbn);
  }
}
