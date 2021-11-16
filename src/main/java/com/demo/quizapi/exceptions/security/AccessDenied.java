package com.demo.quizapi.exceptions.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 09/02/2021 - 3:05 AM
 * @project com.gangarage.coreapi.exceptions.securtiy @ api In AccessDenied
 */

@Component
public class AccessDenied extends CustomFilterResponse implements AccessDeniedHandler {

  @Override
  public void handle(
    HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    AccessDeniedException e
  )
    throws IOException, ServletException {
    new CustomFilterResponse("unauthorized api access", httpServletResponse, e);
  }
}
