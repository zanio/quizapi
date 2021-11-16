package com.demo.quizapi.services.applicationUser;


import com.demo.quizapi.entities.ApplicationUser;

import java.util.Optional;


public interface ApplicationUsers {
  Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
