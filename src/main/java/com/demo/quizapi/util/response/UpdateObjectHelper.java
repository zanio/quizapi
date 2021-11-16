package com.demo.quizapi.util.response;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 06/03/2021 - 9:44 PM
 * @project com.aidingafrica.api.util.objectHelpers @ api In UpdateObjectHelper
 */

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
public final class UpdateObjectHelper {

  public static void updateFieldValue(Object objectDto, Object existingObject)
    throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Field[] fieldsInObjectDto = objectDto.getClass().getDeclaredFields();
    for (Field field : fieldsInObjectDto) {
      field.setAccessible(true);
      Object fieldValue = field.get(objectDto);
      if (fieldValue != null) {
        String fieldName = field.getName();
        String setterFieldName = capitalizeFirstLetter(fieldName, "set");
  log.info("{}", setterFieldName);
        Method method = existingObject
          .getClass()
          .getDeclaredMethod(setterFieldName, field.getType());
        method.setAccessible(true);
        method.invoke(existingObject, fieldValue);
      }
    }
  }

  /**
   * this is the method maps value from one object to another object
   * @param object: this is the object tha is to be mapped
   * @param existingObject: this is object that contains the value that should be mapped
   * @param excludedFields: fields that you want to ignore in the mapping
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   */
  public static void updateFieldValueWithUndefined(
    Object object,
    Object existingObject,
    Set<String> excludedFields
  )
    throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Field[] fieldsInObjectDto = object.getClass().getDeclaredFields();
    Field[] fieldsInExistingObjectDto = existingObject.getClass().getDeclaredFields();
    for (Field field : fieldsInObjectDto) {
      field.setAccessible(true);
      String fieldName = field.getName();
      String setterFieldName = capitalizeFirstLetter(fieldName, "set");
      for (Field exitingField : fieldsInExistingObjectDto) {
        exitingField.setAccessible(true);
        Object fieldValue = exitingField.get(existingObject);
        String fieldNameExisting = exitingField.getName();

        String setterFieldNameExisting = capitalizeFirstLetter(fieldNameExisting, "set");
        if (
          setterFieldNameExisting.equals(setterFieldName) &&
          excludedFields.stream().noneMatch(e -> e.equals(fieldNameExisting))
        ) {
          Method method = object
            .getClass()
            .getDeclaredMethod(setterFieldNameExisting, exitingField.getType());
          method.setAccessible(true);
          method.invoke(object, fieldValue);
        }
      }
    }
  }

  /**
   * this is the method maps value from one object to another object
   * @param object: this is the object tha is to be mapped
   * @param existingObject: this is object that contains the value that should be mapped
   * @param excludedFields: fields that you want to ignore in the mapping
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   */
  public static Object updateFieldValueWithUndefinedReturn(
    Object object,
    Object existingObject,
    Set<String> excludedFields
  )
    throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Field[] fieldsInObjectDto = object.getClass().getDeclaredFields();
    Field[] fieldsInExistingObjectDto = existingObject.getClass().getDeclaredFields();
    for (Field field : fieldsInObjectDto) {
      field.setAccessible(true);
      String fieldName = field.getName();
      String setterFieldName = capitalizeFirstLetter(fieldName, "set");
      for (Field exitingField : fieldsInExistingObjectDto) {
        exitingField.setAccessible(true);
        Object fieldValue = exitingField.get(existingObject);
        String fieldNameExisting = exitingField.getName();

        String setterFieldNameExisting = capitalizeFirstLetter(fieldNameExisting, "set");
        if (
          setterFieldNameExisting.equals(setterFieldName) &&
          excludedFields.stream().noneMatch(e -> e.equals(fieldNameExisting))
        ) {
          Method method = object
            .getClass()
            .getDeclaredMethod(setterFieldNameExisting, exitingField.getType());
          method.setAccessible(true);
          method.invoke(object, fieldValue);
        }
      }
    }
    return object;
  }

  public static String capitalizeFirstLetter(String word, String prefix) {
    String firstLetterOfFieldName = String.valueOf(word.charAt(0)).toUpperCase();
    String subStringWithOutFirstLetter = word.substring(1);
    String fieldName = firstLetterOfFieldName + subStringWithOutFirstLetter;
    return prefix + fieldName;
  }


}
