package com.demo.quizapi.entities;

import com.demo.quizapi.util.HelperClass;
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
public class ApiToken implements Serializable {
  private static final int EXPIRATION = 604_800_000; // 7 days as expiry period

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(unique = true)
  private String token;

  private String name;

  private String type;

  @ManyToOne(fetch = FetchType.EAGER)
  private User baseUser;

  private boolean invalidateToken;

  private Date expiryDate;

  @CreationTimestamp
  private Date dateCreated;

  public Date calculateExpiryDate() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MILLISECOND, ApiToken.EXPIRATION);
    return new Date(cal.getTime().getTime());
  }

  public ApiToken(
    @NotNull(message = "A user must be mapped to a VerificationToken") User baseUser
  ) {
    this.expiryDate = calculateExpiryDate();
    this.token = HelperClass.generateToken();
    this.baseUser = baseUser;
    this.invalidateToken = false;
    this.dateCreated = new Date();
  }

  public boolean isTokenBad() {
    Calendar cal = Calendar.getInstance();
    long time = this.getExpiryDate().getTime() - cal.getTime().getTime();
    return time < 0;
  }
  // standard constructors, getters and setters
}
