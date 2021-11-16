package com.demo.quizapi.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResponseApi {
  private HttpStatus status;
  private Map<String, Object> data;
}
