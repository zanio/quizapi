package com.demo.quizapi.exceptions.custom;

/*
 *@author Aniefiok Akpan
 * created on 07/05/2020
 *
 */

public class UserAlreadyExistException extends Throwable {

  public UserAlreadyExistException(String message) {
    super(message);
  }
}
