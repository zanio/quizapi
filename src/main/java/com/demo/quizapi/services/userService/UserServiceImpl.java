package com.demo.quizapi.services.userService;

import com.demo.quizapi.dao.UserDao;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.exceptions.custom.UserAlreadyExistException;
import com.demo.quizapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private PasswordEncoder passwordEncoderImpl;

    @Autowired
    private UserRepository userRepositoryImpl;
    @Override
    public User create(UserDao userDao) throws UserAlreadyExistException {
        log.info("THE USER WE ARE ONBOARDING IS {}", userDao);
        boolean existingBaseUser = userRepositoryImpl
                .findByEmail(userDao.getEmail())
                .isPresent();
        log.info("existingBaseUser -> {}", existingBaseUser);
        if (existingBaseUser) {
            throw new UserAlreadyExistException("Email Already Exist");
        }
        User user = new User();
        user.setEmail(userDao.getEmail());
        user.setFirstName(userDao.getFirstName());
        user.setLastName(userDao.getLastName());
        user.setPassword(passwordEncoderImpl.encode(userDao.getPassword()));
        userRepositoryImpl.save(user);
        return user;
    }
}
