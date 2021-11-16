package com.demo.quizapi.entities;

import com.demo.quizapi.util.HelperClass;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 29/01/2021 - 11:15 PM
 * @project com.gangarage.coreapi.model.verificationToken @ api In VerificationToken
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Question implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String value;

  private Boolean active;

  @OneToMany(mappedBy ="question")
  @JsonManagedReference
  private Set<Option> options;

  @CreationTimestamp
  private Date dateCreated;


  // standard constructors, getters and setters
}
