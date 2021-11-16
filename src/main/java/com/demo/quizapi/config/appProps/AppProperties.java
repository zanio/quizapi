package com.demo.quizapi.config.appProps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 05/02/2021 - 11:15 PM
 * @project com.gangarage.coreapi.config.appProps @ api In AppProperties
 */

@ConfigurationProperties(prefix = "application.api")
@Getter
@Setter
@ToString
public class AppProperties {
  private String emailSender;
  private String awsAccessKey;
  private String awsSecretKey;
  private String awsEmail;
  private String host;
  private String port;
  private String username;
  private String password;
  private String smtpStarttlsPropertiesEnable;
  private String smtpPropertiesAuth;
  private String sslEnabled;
  private String flywayLocation;
  private String redisHost;
  private String redisPort;
  private Double commissionOnDonation;
  private Double transactionFee;
  private String openExchangeAppId;
  private String defaultTtl;
  private int redisDatabase;
  private Map<String, String> cacheTtl;
  private Map<String, String> pusher;
  private Map<String, String> africaTalking;
  private Map<String, String> rabbitmq;
  private Map<String, String> placeholders;
}
