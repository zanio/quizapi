package com.demo.quizapi.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Setter
@Getter
@Builder
public class UserDao {
  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email(message = "Email is not valid")
  private String email;

  @NotBlank
  private String password;}
