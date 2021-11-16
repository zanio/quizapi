package com.demo.quizapi.exceptions.custom;

/*
 *@author tobi
 * created on 07/05/2020
 *
 */


public class FieldIsRequiredException extends RuntimeException {

  public FieldIsRequiredException(String message) {
    super(message);
  }
}
