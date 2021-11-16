package com.demo.quizapi.services.applicationUser;

import com.demo.quizapi.entities.ApplicationUser;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 6:43 PM
 * @project com.gangarage.coreapi.services.dao @ api In ApplicationUserDaoImpl
 */

@Service("user_app_1")
@Slf4j
public class ApplicationUsersImpl implements ApplicationUsers {
  private final UserRepository userRepositoryImpl;

  @Autowired
  public ApplicationUsersImpl(UserRepository userRepositoryImpl) {
    this.userRepositoryImpl = userRepositoryImpl;
  }

  @Override
  public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
    Optional<User> users = userRepositoryImpl.findByEmail(username);
    return Optional.of(new ApplicationUser(users.orElse(null)));
  }
}
