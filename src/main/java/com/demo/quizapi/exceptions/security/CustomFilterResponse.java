package com.demo.quizapi.exceptions.security;

import com.demo.quizapi.util.HelperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class CustomFilterResponse {
  private String message;


  public CustomFilterResponse(
    String message,
    HttpServletResponse httpServletResponse,
    Exception e
  )
    throws IOException, ServletException {
    errorProperty(message, httpServletResponse, e);
  }

  private void errorProperty(
    String message,
    HttpServletResponse httpServletResponse,
    Exception e
  )
    throws IOException {
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpServletResponse.setHeader(
      "Content-type: application/json",
      "Accept: application/json"
    );

    log.error(
      "AuthenticationEntryPoint error => {} and error type {}",
      e.getLocalizedMessage(),
      e.getClass().getName()
    );
    this.message = message;
    int statusCode = httpServletResponse.getStatus();

    Map<String, Object> response = new HashMap<>();
    if (e instanceof BadCredentialsException) {
      statusCode = HttpStatus.BAD_REQUEST.value();
    } else if (e instanceof InsufficientAuthenticationException) {
      statusCode = HttpStatus.BAD_REQUEST.value();
    }
    response.put("statusCode", statusCode);
    response.put("statusMessage", this.message);
    response.put("message", e.getLocalizedMessage());
    ObjectMapper mapper = new ObjectMapper();
    log.info(
      "The error object {} unauthorized and the status is {}",
      response.values(),
      httpServletResponse.getStatus()
    );
    OutputStream out = httpServletResponse.getOutputStream();
    mapper
      .writerWithDefaultPrettyPrinter()
      .writeValue(out, HelperClass.convObjToONode(response));
    out.flush();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(@NotNull String message) {
    this.message = message;
  }
}
