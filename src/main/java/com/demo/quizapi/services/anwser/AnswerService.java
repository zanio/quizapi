package com.demo.quizapi.services.anwser;

import com.demo.quizapi.dao.AnswerDao;
import com.demo.quizapi.entities.Answer;
import com.demo.quizapi.entities.User;

import java.util.Set;

public interface AnswerService {
    Answer create(AnswerDao answerDao);
    Answer update(Integer answerId,Integer optionId);
    Answer findOne(Integer answerId);
    Set<Answer> getAllCorrectAnswer();
    Set<Answer> getAllWrongAnswer();
    Long totalNumberOfQuestionsAnswered();

}
