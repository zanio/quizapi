package com.demo.quizapi.util.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 11/02/2021 - 11:45 AM
 * @project com.gangarage.coreapi.util.response @ api In LowerCaseClassNameResolver
 */

public class LowerCaseClassNameResolver extends TypeIdResolverBase {

  @Override
  public String idFromValue(Object value) {
    return value.getClass().getSimpleName().toLowerCase();
  }

  @Override
  public String idFromValueAndType(Object value, Class<?> suggestedType) {
    return idFromValue(value);
  }

  @Override
  public JsonTypeInfo.Id getMechanism() {
    return JsonTypeInfo.Id.CUSTOM;
  }
}
