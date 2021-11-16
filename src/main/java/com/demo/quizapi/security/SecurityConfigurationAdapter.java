package com.demo.quizapi.security;

import com.demo.quizapi.config.context.SecurityContextConfig;
import com.demo.quizapi.config.jwt.JwtConfig;
import com.demo.quizapi.exceptions.security.AccessDenied;
import com.demo.quizapi.exceptions.security.RestAuthenticationFailureHandler;
import com.demo.quizapi.services.jwt.JwtTokenVerifier;
import com.demo.quizapi.services.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.demo.quizapi.services.userDetailsService.ApplicationUserServiceImpl;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 29/01/2021 - 12:45 AM
 * @project com.gangarage.coreapi.security @ api In SecurityConfigurationAdapter
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
  prePostEnabled = true,
  securedEnabled = true,
  jsr250Enabled = true
)
@Slf4j
@Order(1000)
public class SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
  private final PasswordEncoder passwordEncoder;
  private final ApplicationUserServiceImpl applicationUserService;
  private final SecretKey secretKey;
  private final JwtConfig jwtConfig;
  private final Environment env;
  private final SecurityContextConfig securityContextConfig;
  private final AuthenticationEntryPoint authenticationEntryPointImpl;

  @Value("${client.url}")
  private String clientDevUrl;

  @Value("${client.url}")
  private String clientProdUrl;


  @Autowired
  public SecurityConfigurationAdapter(
    PasswordEncoder passwordEncoder,
    ApplicationUserServiceImpl applicationUserService,
    SecretKey secretKey,
    JwtConfig jwtConfig,
    Environment env,
    SecurityContextConfig securityContextConfig,
    @Qualifier(
      "customRestAuthenticationEntryPoint"
    ) AuthenticationEntryPoint authenticationEntryPointImpl
  ) {
    this.passwordEncoder = passwordEncoder;
    this.applicationUserService = applicationUserService;
    this.secretKey = secretKey;
    this.jwtConfig = jwtConfig;
    this.env = env;

    this.securityContextConfig = securityContextConfig;
    this.authenticationEntryPointImpl = authenticationEntryPointImpl;
  }

  private static final RequestMatcher PERMITTED_URLS = new OrRequestMatcher(
    new AntPathRequestMatcher(SecurityConstants.SIGN_UP_URL, "POST")
  );


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
      .disable()
      .authorizeRequests()
      //                .requestMatchers(DENY_ALL_URLS).denyAll()
      .requestMatchers(PERMITTED_URLS)
      .permitAll()
      .antMatchers(SecurityConstants.DEFAULT)
      .hasAnyRole(
        "USER"
      )
      .anyRequest()
      .authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .cors()
      .configurationSource(corsConfigurationSource())
      .and()
      .exceptionHandling()
      .authenticationEntryPoint(authenticationEntryPointImpl)
      .accessDeniedHandler(accessDeniedHandler())
      .and()
      .addFilter(
        new JwtUsernameAndPasswordAuthenticationFilter(
          authenticationManager(),
          jwtConfig,
          env,
          secretKey,
          getApplicationContext()
        )
      )
      .addFilterAfter(
        new JwtTokenVerifier(
          secretKey,
          getApplicationContext(),
          securityContextConfig,
          jwtConfig
        ),
        JwtUsernameAndPasswordAuthenticationFilter.class
      )
      .logout()
      .disable();

    http
      .requiresChannel()
      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
      .requiresSecure();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(applicationUserService);
    return provider;
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new AccessDenied();
  }

  //    @Bean
  //    public AuthenticationEntryPoint authenticationEntryPoint(){
  //        return new EntryPoint();
  //    }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new RestAuthenticationFailureHandler();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
      Arrays.asList(clientDevUrl, clientProdUrl, "http://127.0.0.1:5500")
    );
    configuration.setAllowedMethods(
      Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE")
    );
    configuration.setAllowCredentials(true);
    //the below three lines will add the relevant CORS response headers
    configuration.addAllowedHeader("*");
    configuration.setAllowedHeaders(
      ImmutableList.of("Authorization", "Cache-Control", "Content-Type")
    );
    configuration.addAllowedHeader("Authorization");
    configuration.addExposedHeader("Authorization");
    configuration.addExposedHeader("Access-Control-Allow-Origin");
    configuration.addExposedHeader("Access-Control-Allow-Headers");
    configuration.addExposedHeader("ETag");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
