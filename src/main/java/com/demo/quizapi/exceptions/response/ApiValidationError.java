package com.demo.quizapi.exceptions.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 11/02/2021 - 11:41 AM
 * @project com.gangarage.coreapi.exceptions.generic @ api In ApiValidationError
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationError(String object, String message) {
    this.object = object;
    this.message = message;
  }
}
