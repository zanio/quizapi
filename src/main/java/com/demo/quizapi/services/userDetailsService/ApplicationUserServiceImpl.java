package com.demo.quizapi.services.userDetailsService;

import com.demo.quizapi.entities.ApplicationUser;
import com.demo.quizapi.services.applicationUser.ApplicationUsers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author with Username zanio and fullname ANIEFIOK AKPAN
 * @created 28/01/2021 - 7:05 PM
 * @project com.gangarage.coreapi.services.userDetailsService @ api In ApplicationUserServiceImpl
 */

@Service
@Slf4j
public class ApplicationUserServiceImpl implements UserDetailsService {
  private final ApplicationUsers applicationUsersImpl;

  @Autowired
  public ApplicationUserServiceImpl(
    @Qualifier("user_app_1") ApplicationUsers applicationUsersImpl
  ) {
    this.applicationUsersImpl = applicationUsersImpl;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<ApplicationUser> userOptional = applicationUsersImpl.selectApplicationUserByUsername(
      username
    );

    if (!ObjectUtils.allNotNull(userOptional.get().getUsers())) {
      log.error("APPLICATION USER {} NOT FOUND", username);
      throw new UsernameNotFoundException(
        String.format("APPLICATION USER'%s' NOT FOUND", username)
      );
    }
    return userOptional.get();
  }
}
