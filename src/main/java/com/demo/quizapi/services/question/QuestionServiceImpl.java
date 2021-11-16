package com.demo.quizapi.services.question;

import com.demo.quizapi.entities.Question;
import com.demo.quizapi.exceptions.custom.EntityNotFoundException;
import com.demo.quizapi.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Override
    public Set<Question> getQuestions() {
        return Streamable.of(questionRepository.findAll()).toSet();
    }

    @Override
    public Question getQuestion(Integer id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Question.class, "Id", id.toString())
        );
    }

}
