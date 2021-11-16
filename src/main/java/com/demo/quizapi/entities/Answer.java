package com.demo.quizapi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
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
public class Answer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @ManyToOne
  private Option option;

  @ManyToOne
  private User user;

  @CreationTimestamp
  private Date dateCreated;


  // standard constructors, getters and setters
}
