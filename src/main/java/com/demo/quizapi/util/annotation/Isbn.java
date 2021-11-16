package com.demo.quizapi.util.annotation;

import com.demo.quizapi.validator.IsbnValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsbnValidator.class)
@Target(
  {
    ElementType.METHOD,
    ElementType.FIELD,
    ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE
  }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn {
  String locale() default "";

  String message() default "Invalid isbn";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
