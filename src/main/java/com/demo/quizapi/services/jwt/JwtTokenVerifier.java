package com.demo.quizapi.services.jwt;

import com.demo.quizapi.config.context.SecurityContextConfig;
import com.demo.quizapi.config.jwt.JwtConfig;
import com.demo.quizapi.entities.ApiToken;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.repository.ApiTokenRepository;
import com.demo.quizapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 7:33 PM
 * @project com.gangarage.coreapi.services.jwt @ api In JwtTokenVerifier
 */

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {
  private final SecretKey secretKey;
  private final JwtConfig jwtConfig;
  private final SecurityContextConfig securityContextConfig;
  private final UserRepository userRepositoryImpl;
  private final ApiTokenRepository apitokenRepositoryImpl;
  public JwtTokenVerifier(
    SecretKey secretKey,
    ApplicationContext applicationContext,
    SecurityContextConfig securityContextConfig,
    JwtConfig jwtConfig
  ) {
    this.secretKey = secretKey;
    this.jwtConfig = jwtConfig;
    this.securityContextConfig = securityContextConfig;
    this.userRepositoryImpl = applicationContext.getBean(UserRepository.class);
    this.apitokenRepositoryImpl = applicationContext.getBean(ApiTokenRepository.class);

  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  )
    throws ServletException, IOException {
    String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

    if (
      Strings.isNullOrEmpty(authorizationHeader) ||
      !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())
    ) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

    try {
      ObjectMapper objectMapper = new ObjectMapper();

      Jws<Claims> claimsJws = Jwts
        .parserBuilder()
        .deserializeJsonWith(new JacksonDeserializer(objectMapper))
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);

      Claims body = claimsJws.getBody();

      String username = body.getSubject();
      ApiToken apiToken = apitokenRepositoryImpl.findByToken(token);
      if (apiToken == null) {
        String s = "Invalid api token " + username;
        log.error(s);
        securityContextConfig.blockerResponse(response, s, 403);
        return;
      }
      if (apiToken.isTokenBad() || apiToken.isInvalidateToken()) {
        apiToken.setInvalidateToken(true);
        String s = "api token is expired " + username;
        apitokenRepositoryImpl.save(apiToken);
        log.error(s);
        securityContextConfig.blockerResponse(response, s, 403);
        return;
      }
      Optional<User> userOptional = userRepositoryImpl.findByEmail(username);
      if (userOptional.isEmpty()) {
        String s = "The user does not exist " + username;
        log.error(s);
        securityContextConfig.blockerResponse(response, s, 403);
        return;
      }
      User user = userOptional.get();
      Set<SimpleGrantedAuthority> authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_" + "USER"));


      log.info(
        " The authorities is as follows {}",
        Arrays.toString(authorities.toArray())
      );

      Authentication authentication = new UsernamePasswordAuthenticationToken(
        username,
        null,
        authorities
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException e) {
      var msg = "Token %s cannot be trusted";
      securityContextConfig.blockerResponse(response, String.format(msg, token), 403);
      log.error(msg);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
