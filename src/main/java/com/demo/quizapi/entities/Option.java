package com.demo.quizapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
public class Option implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String value;

  private boolean correct;

  @ManyToOne
  @ToString.Exclude
  @JoinColumn(name = "question_id")
  @JsonBackReference
  private Question question;

  @CreationTimestamp
  private Date dateCreated;


  // standard constructors, getters and setters
}
