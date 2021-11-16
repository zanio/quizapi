package com.demo.quizapi.repository;

import com.demo.quizapi.entities.ApiToken;
import com.demo.quizapi.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(properties = "spring.profiles.active=test")
@Slf4j
class ApiTokenRepositoryTest {
    @Autowired
    ApiTokenRepository apiTokenRepository;
    @Autowired
    UserRepository userRepositoryImpl;

    @Test
    void testApiTokenCanBeSaved(){
        ApiToken apiToken = new ApiToken();
        User user1 = new User();
        user1.setLastName("name1");
        user1.setFirstName("name2");
        user1.setPassword("good");
        user1.setEmail("ani@yahoo.com");
        userRepositoryImpl.save(user1);
        apiToken.setName("test_token");
        apiToken.setBaseUser(user1);
        apiTokenRepository.save(apiToken);
        assertThat(apiToken.getId()).isNotNull();
    }

    @Test
    void testApiTokenCanBeFound(){
        ApiToken apiToken = new ApiToken();
        User user1 = new User();
        user1.setLastName("name1");
        user1.setFirstName("name2");
        user1.setPassword("good");
        user1.setEmail("anit@yahoo.com");
        userRepositoryImpl.save(user1);
        apiToken.setName("test_token");
        apiToken.setBaseUser(user1);
        apiTokenRepository.save(apiToken);
        ApiToken foundApi =  apiTokenRepository.findByToken(apiToken.getToken());
        assertThat(foundApi).isNotNull();
    }
}