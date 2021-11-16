package com.demo.quizapi.services.question;

import com.demo.quizapi.entities.Question;

import java.util.Set;

public interface QuestionService {
    Set<Question> getQuestions();
   Question getQuestion(Integer id);
}
