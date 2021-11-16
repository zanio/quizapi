package com.demo.quizapi.exceptions.custom;

/*
 *@author tobi
 * created on 07/05/2020
 *
 */


import com.demo.quizapi.util.HelperClass;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(Class clazz, String... searchParamsMap) {
    super(
      HelperClass.generateMessage(
        clazz.getSimpleName(),
        HelperClass.toMap(String.class, String.class, searchParamsMap)
      )
    );
  }
}
