package com.demo.quizapi.repository;

import com.demo.quizapi.entities.ApiToken;
import com.demo.quizapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ApiTokenRepository extends JpaRepository<ApiToken, Integer> {
  ApiToken findByToken(String token);
  ApiToken findByTokenAndBaseUser(String token, User user);
}
