package com.demo.quizapi.config.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 4:23 PM
 * @project com.gangarage.coreapi.config.jwt @ api In JwtSecretKeyConfig
 */

@Configuration
public class JwtSecretKeyConfig {
  private final JwtConfig jwtConfig;

  @Autowired
  public JwtSecretKeyConfig(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
  }
}
