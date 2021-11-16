package com.demo.quizapi.repository;

import com.demo.quizapi.entities.Answer;
import com.demo.quizapi.entities.Option;
import com.demo.quizapi.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, Integer> {

    Set<Answer> findByOption_CorrectAndUser(boolean isCorrect, User user);
    Answer findByUserAndOption(User user, Option option);
}
