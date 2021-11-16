package com.demo.quizapi.repository;

import com.demo.quizapi.entities.Answer;
import com.demo.quizapi.entities.Option;
import com.demo.quizapi.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(properties = "spring.profiles.active=test")
@Slf4j
@Sql(scripts = {"classpath:migration/dev/V2__INSERT_QUESTIONS.sql", "classpath:migration/dev/V3__INSERT_OPTIONS.sql"})
@Sql(scripts = "classpath:clean-up.sql", executionPhase = AFTER_TEST_METHOD)
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepositoryImpl;

    @Autowired
     OptionRepository optionRepositoryImpl;

    @Autowired
    UserRepository userRepositoryImpl;
    Answer answer1;
    Answer answer2;
    User user1;
    @BeforeEach
    void setUp() {
        answer1 = new Answer();
        answer2 = new Answer();
        user1 = new User();
        user1.setLastName("name1");
        user1.setFirstName("name2");
        user1.setPassword("good");
        user1.setEmail("ani@yahoo.com");
        userRepositoryImpl.save(user1);
        Option option1 =optionRepositoryImpl.findById(3).orElse(null);
        Option option2 =optionRepositoryImpl.findById(4).orElse(null);
        answer1.setOption(option1);
        answer1.setUser(user1);
        answerRepositoryImpl.save(answer1);
        answer2.setUser(user1);
        answer2.setOption(option2);
        answerRepositoryImpl.save(answer2);
    }

    @AfterEach
    void tearDown() {
        answerRepositoryImpl.deleteAll(Set.of(answer1,answer2));
        userRepositoryImpl.delete(user1);
    }
    @Test
    void testThatAnswerCanBeSaved(){
        User user = new User();
        user.setLastName("name1");
        user.setFirstName("name2");
        user.setPassword("good");
        user.setEmail("anis@yahoo.com");
        userRepositoryImpl.save(user);
        Answer answer = new Answer();
        Option option1 =optionRepositoryImpl.findById(3).orElse(null);
        answer.setOption(option1);
        answer.setUser(user);
        answerRepositoryImpl.save(answer);
        assertThat(answer.getId()).isNotNull();
    }
    @Test
    void testThatAnswerCanBeFound(){
        Optional<Answer> answerOptional =answerRepositoryImpl.findById(answer1.getId());
        assertThat(answerOptional.isPresent()).isNotNull();
    }

    @Test
    void testThat_findByOption_CorrectAndUser_returns_set(){
        Answer answer = new Answer();
        User user = new User();
        user.setLastName("name1");
        user.setFirstName("name2");
        user.setPassword("good");
        user.setEmail("aniw@yahoo.com");
        userRepositoryImpl.save(user);
        Option option1 =optionRepositoryImpl.findById(7).orElse(null);
        answer.setOption(option1);
        answer.setUser(user);
        answerRepositoryImpl.save(answer);
        Set<Answer> answers = answerRepositoryImpl.findByOption_CorrectAndUser(true,user1);
        assertThat(answers.size()).isEqualTo(0);
    }

    @Test
    void testThat_findByOption_WrongAndUser_returns_set(){
        Answer answer = new Answer();
        User user = new User();
        user.setLastName("name1");
        user.setFirstName("name2");
        user.setPassword("good");
        user.setEmail("aniw@yahoo.com");
        userRepositoryImpl.save(user);
        Option option1 =optionRepositoryImpl.findById(3).orElse(null);
        answer.setOption(option1);
        answer.setUser(user);
        answerRepositoryImpl.save(answer);
        Set<Answer> answers = answerRepositoryImpl.findByOption_CorrectAndUser(false,user1);
        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    void testThat_UserAndOptionCanBeFound(){
        Answer answer = new Answer();
        User user = new User();
        user.setLastName("name1");
        user.setFirstName("name2");
        user.setPassword("good");
        user.setEmail("aniw@yahoo.com");
        userRepositoryImpl.save(user);
        Option option1 =optionRepositoryImpl.findById(3).orElse(null);
        answer.setOption(option1);
        answer.setUser(user);
        answerRepositoryImpl.save(answer);
        Answer answerInDb = answerRepositoryImpl.findByUserAndOption(user,option1);
        assertThat(answerInDb).isNotNull();
    }
}