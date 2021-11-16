package com.demo.quizapi.config.jwt;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 3:54 PM
 * @project com.gangarage.coreapi.config.jwt @ api In JwtConfig
 */

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "application.jwt")
@NoArgsConstructor
public class JwtConfig {
  private String secretKey;
  private String tokenPrefix;
  private Integer tokenExpirationAfterDays;

  public String getAuthorizationHeader() {
    return HttpHeaders.AUTHORIZATION;
  }
}
