package com.demo.quizapi.config.flyway;

import com.demo.quizapi.config.appProps.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class FlywayConfiguration {

  @Autowired
  public FlywayConfiguration(DataSource dataSource, AppProperties appProperties) {
    log.warn("THE APPLICATION PROPERTIES FILE IS AS FOLLOWS::: ==> {}", appProperties);

    Flyway
      .configure()
      .baselineOnMigrate(true)
      .locations(appProperties.getFlywayLocation())
      .dataSource(dataSource)
      .load()
      .migrate();
  }
}
