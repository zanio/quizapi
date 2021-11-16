package com.demo.quizapi.config.context;

import com.demo.quizapi.entities.User;
import com.demo.quizapi.exceptions.custom.EntityNotFoundException;
import com.demo.quizapi.repository.UserRepository;
import com.demo.quizapi.util.HelperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


@Configuration
@Slf4j
public class SecurityContextConfig {
  private final UserRepository userRepositoryImpl;
  private final HttpServletRequest request;

  @Value("spring.profiles.active")
  private String activeProfile;


  public SecurityContextConfig(
    UserRepository userRepositoryImpl,
    HttpServletRequest request
  ) {
    this.userRepositoryImpl = userRepositoryImpl;
    this.request = request;
  }

  public String getBaseUrl() {
    log.info("Request request uri {}", request.getRequestURI());
    log.info("Request request url {}", request.getRequestURL());
    log.info("Request header host {}", request.getHeader("host"));
    log.info("Request header origin {}", request.getHeader("origin"));
    log.info("Request scheme {}", request.getScheme());
    log.info("Request servletPath {}", request.getServletPath());
    log.info("Request server name {}", request.getServerName());
    log.info("Request remote address {}", request.getRemoteAddr());
    //        log.info("Request remote address {}", request.ge);

    String getDomainNameWithPort = request.getHeader("origin");

    String baseUrl = getDomainNameWithPort;
    if (baseUrl == null) {
      baseUrl = request.getScheme() + "://" + request.getHeader("host");
    }

    log.info("Base url is -->{}", baseUrl);
    log.info("Base url with port if there is one:  -->{}", getDomainNameWithPort);

    return baseUrl + "/";
  }

  public Locale getLocale() {
    return request.getLocale();
  }

  public void blockerResponse(HttpServletResponse response, String message, int code)
    throws IOException {
    response.setHeader("Content-Type: application/json", "Accept: application/json");
    response.setStatus(code);
    Map<String, Object> responseObject = new HashMap<>();
    responseObject.put("status", response.getStatus());
    responseObject.put("message", message);

    ObjectMapper mapper = new ObjectMapper();
    OutputStream out = response.getOutputStream();
    mapper
      .writerWithDefaultPrettyPrinter()
      .writeValue(out, HelperClass.convObjToONode(responseObject));
    out.flush();
    out.close();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    return builder.build();
  }

  /**
   * This method returns the email address of the user
   * @return
   */
  public String getSecurityContext() {
    return SecurityContextHolder.getContext().getAuthentication() != null
            ? (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            : null;
  }

  public User getUserExistInSecurityContext() {
    String email = getSecurityContext();
    log.info("email -> {}", email);
    Optional<User> findUserByEmail = userRepositoryImpl.findByEmail(email);
    if (findUserByEmail.isEmpty()) {
      throw new EntityNotFoundException(User.class, "email", email);
    }
    return findUserByEmail.get();
  }
}
