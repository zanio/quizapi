package com.demo.quizapi.services.userService;

import com.demo.quizapi.dao.UserDao;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.exceptions.custom.UserAlreadyExistException;

public interface UserService {
    User create(UserDao userDao) throws UserAlreadyExistException;
}
