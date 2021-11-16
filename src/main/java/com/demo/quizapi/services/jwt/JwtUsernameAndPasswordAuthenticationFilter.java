package com.demo.quizapi.services.jwt;

import com.demo.quizapi.config.jwt.JwtConfig;
import com.demo.quizapi.dao.UsernameAndPasswordAuthenticationDto;
import com.demo.quizapi.entities.ApiToken;
import com.demo.quizapi.entities.ApplicationUser;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.repository.ApiTokenRepository;
import com.demo.quizapi.repository.UserRepository;
import com.demo.quizapi.util.HelperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter
  extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;
  private Environment env;
  private final ApiTokenRepository apitokenRepositoryImpl;
  private final UserRepository userRepositoryImpl;

  public JwtUsernameAndPasswordAuthenticationFilter(
    AuthenticationManager authenticationManager,
    JwtConfig jwtConfig,
    Environment env,
    SecretKey secretKey,
    ApplicationContext applicationContext
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtConfig = jwtConfig;
    this.secretKey = secretKey;
    this.env = env;
    this.apitokenRepositoryImpl = applicationContext.getBean(ApiTokenRepository.class);
    this.userRepositoryImpl = applicationContext.getBean(UserRepository.class);

    String LOGIN_PATH = "/api/v1/login";
    setFilterProcessesUrl(LOGIN_PATH);
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws AuthenticationException {
    try {
      UsernameAndPasswordAuthenticationDto authenticationRequest = new ObjectMapper()
      .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationDto.class);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
        authenticationRequest.getUsername(),
        authenticationRequest.getPassword()
      );

      return authenticationManager.authenticate(authentication);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    //
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  )
    throws IOException, ServletException {
    this.loginNotification(authResult, request);
    ObjectMapper objectMapper = new ObjectMapper();

    ApplicationUser applicationUser = ((ApplicationUser) authResult.getPrincipal());
    String email = applicationUser.getUsers().getEmail();

    String token = Jwts
      .builder()
      .serializeToJsonWith(new JacksonSerializer(objectMapper))
      .setSubject(email)
      .setIssuedAt(new Date())
      .setExpiration(
        java.sql.Date.valueOf(
          LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())
        )
      )
      .signWith(secretKey)
      .compact();

    prepareResponseAfterSuccessFullLogin(
      response,
      authResult,
      email,
      token,
      applicationUser.getAuthorities()
    );
  }

  private void prepareResponseAfterSuccessFullLogin(
    HttpServletResponse response,
    Authentication authResult,
    String email,
    String token,
    Collection<? extends GrantedAuthority> authorities
  )
    throws IOException {
    log.info("the token {}", token);

    log.info("user authorities ---> {}", authResult.getAuthorities());
    log.info("user email ---> {}", email);
    log.info("user details ---> {}", authResult.getDetails());

    response.setHeader("Content-Type: application/json", "Accept: application/json");
    ApiToken apiToken = new ApiToken();
    apiToken.setToken(token);
    apiToken.setExpiryDate(apiToken.calculateExpiryDate());
    Optional<User> baseUser = userRepositoryImpl.findByEmail(email);
    baseUser.ifPresent(apiToken::setBaseUser);
    apiToken.setName("login token");
    apiToken.setType("successful_login");
    apitokenRepositoryImpl.save(apiToken);
    Map<String, Object> responseObject = new HashMap<>();
    responseObject.put(
      jwtConfig.getAuthorizationHeader(),
      jwtConfig.getTokenPrefix() + token
    );
    responseObject.put("authorities", authorities);
    ResponseEntity<?> responseEntity = HelperClass.generateResponse(
      "login successful",
      HttpStatus.OK,
      responseObject
    );
    ObjectMapper mapper = new ObjectMapper();
    OutputStream out = response.getOutputStream();
    mapper.writerWithDefaultPrettyPrinter().writeValue(out, responseEntity.getBody());
    out.flush();
    out.close();
  }

  private void loginNotification(
    Authentication authentication,
    HttpServletRequest request
  ) {
    try {
      log.info(
        "authentication.getPrincipal " +
        "and instance of User {} and what is the  authentication.getPrincipal {}",
        authentication.getPrincipal().getClass(),
        authentication.getPrincipal()
      );
    } catch (Exception e) {
      logger.error("An error occurred while verifying device or location", e);
      throw new RuntimeException(e);
    }
  }

}
