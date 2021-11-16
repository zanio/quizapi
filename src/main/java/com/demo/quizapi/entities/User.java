package com.demo.quizapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 5:55 PM
 * @project com.gangarage.coreapi.model.applicationUser @ api In ApplicationUser
 */

@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "base_user")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotBlank
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private String firstName;

  @NotBlank
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private String lastName;

  @Column(unique = true)
  @Email(message = "Email is not valid")
  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private String email;

  @NotBlank
  @ToString.Exclude
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @CreationTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Column(updatable = false)
  private Date createdAt;

  @UpdateTimestamp
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Date updatedAt;
}
