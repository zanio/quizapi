package com.demo.quizapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public final class HelperClass {


  /**
   * This method convert raw object to pojo
   * @param type
   * @param map
   * @param <T>
   * @param <R>
   * @return
   */
  public static <T, R> T convertMapObjectToPojo(Class<T> type, R map) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(map, type);
  }

  /**
   * This object
   * @param keyType
   * @param valueType
   * @param entries
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K, V> Map<K, V> toMap(
    Class<K> keyType,
    Class<V> valueType,
    Object... entries
  ) {
    if (entries.length % 2 == 1) throw new IllegalArgumentException("Invalid entries");
    return IntStream
      .range(0, entries.length / 2)
      .map(i -> i * 2)
      .collect(
        HashMap::new,
        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
        Map::putAll
      );
  }

  /**
   *
   * @param entity
   * @param searchParams
   * @return
   */
  public static String generateMessage(String entity, Map<String, String> searchParams) {
    return (
      StringUtils.capitalize(entity) + " was not found for parameters " + searchParams
    );
  }

  /**
   *
   * @param entity
   * @param searchParams
   * @return
   */
  public static String generateMessage(
    String entity,
    Map<String, String> searchParams,
    String message
  ) {
    return (StringUtils.capitalize(entity) + " " + message + searchParams);
  }

  public static ResponseEntity<?> generateResponse(
    String message,
    HttpStatus status,
    Object responseObj
  ) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("message", message);
    map.put("status", status.value());
    map.put("ResponseData", responseObj);
    return new ResponseEntity<>(map, status);
  }

  public static String stringify(String message, Object... object) {
    return object.length > 0 ? String.format(message, object) : message;
  }

  public static boolean isValidISBN(String isbn)
  {
    // length must be 10
    int n = isbn.length();
    if (n != 10)
      return false;

    // Computing weighted sum
    // of first 9 digits
    int sum = 0;
    for (int i = 0; i < 9; i++)
    {
      int digit = isbn.charAt(i) - '0';
      if (0 > digit || 9 < digit)
        return false;
      sum += (digit * (10 - i));
    }

    // Checking last digit.
    char last = isbn.charAt(9);
    if (last != 'X' && (last < '0' ||
            last > '9'))
      return false;

    // If last digit is 'X', add 10
    // to sum, else add its value
    sum += ((last == 'X') ? 10 : (last - '0'));

    // Return true if weighted sum
    // of digits is divisible by 11.
    return (sum % 11 == 0);
  }

  public static UUID getIdInUUID(@PathVariable("serviceId") String serviceId) {
    return UUID.fromString(serviceId);
  }

  public static ObjectNode convObjToONode(Object o) {
    StringWriter stringify = new StringWriter();
    ObjectNode objToONode = null;
    ObjectMapper mapper = new ObjectMapper();

    try {
      mapper.writeValue(stringify, o);
      objToONode = (ObjectNode) mapper.readTree(stringify.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return objToONode;
  }

  public static String generateToken() {
    return RandomStringUtils.random(250, true, true);
  }

}
