package com.demo.quizapi.clients;

import com.demo.quizapi.dao.AnswerDao;
import com.demo.quizapi.dao.UserDao;
import com.demo.quizapi.entities.Answer;
import com.demo.quizapi.entities.Question;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.exceptions.custom.UserAlreadyExistException;
import com.demo.quizapi.repository.AnswerRepository;
import com.demo.quizapi.repository.QuestionRepository;
import com.demo.quizapi.services.anwser.AnswerService;
import com.demo.quizapi.services.question.QuestionService;
import com.demo.quizapi.services.userService.UserService;
import com.demo.quizapi.util.HelperClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/")
public class AppController {
    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private AnswerService answerServiceImpl;

    @Autowired
    private QuestionService questionServiceImpl;


    @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDao userDao)
            throws UserAlreadyExistException  {
        User user = this.userServiceImpl.create(userDao);
        return HelperClass.generateResponse(
                "registration was successful",
                HttpStatus.CREATED,
                user
        );
    }

    @GetMapping(value = "/questions")
    public ResponseEntity<?> getQuestions() {
       Set<Question> questions = questionServiceImpl.getQuestions();
        return HelperClass.generateResponse(
                "Questions successfully retrieved ",
                HttpStatus.OK,
                questions
        );
    }

    @GetMapping(value = "/questions/{id}")
    public ResponseEntity<?> getQuestion(
            @PathVariable("id") @NotBlank String id
    ) {
        Question questions = questionServiceImpl.getQuestion(Integer.parseInt(id));
        return HelperClass.generateResponse(
                "Question successfully retrieved ",
                HttpStatus.OK,
                questions
        );
    }

    @PostMapping(value = "/answer")
    public ResponseEntity<?> answerQuestion(@RequestBody @Valid AnswerDao answerDao)
              {
        Answer answer = this.answerServiceImpl.create(answerDao);
        return HelperClass.generateResponse(
                "question successfully answered",
                HttpStatus.CREATED,
                answer
        );
    }

    @GetMapping(value = "/answer/{answerId}")
    public ResponseEntity<?> getAnswer(@PathVariable("answerId") @NotBlank String answerId )
    {
        Answer answer = this.answerServiceImpl.findOne(Integer.parseInt(answerId));
        return HelperClass.generateResponse(
                "question successfully retrieved",
                HttpStatus.OK,
                answer
        );
    }
    @GetMapping(value = "/answer/correct")
    public ResponseEntity<?> getAllCorrectAnswer()
    {
        Set<Answer> answers = this.answerServiceImpl.getAllCorrectAnswer();
        return HelperClass.generateResponse(
                "registration was successful",
                HttpStatus.OK,
                answers
        );
    }
    @GetMapping(value = "/answer/wrong")
    public ResponseEntity<?> getAllWrongAnswer()
    {
        Set<Answer> answers = this.answerServiceImpl.getAllWrongAnswer();
        return HelperClass.generateResponse(
                "registration was successful",
                HttpStatus.OK,
                answers
        );
    }
    @GetMapping(value = "/answer/total")
    public ResponseEntity<?> getTotalAnswer()
    {
        long count = this.answerServiceImpl.totalNumberOfQuestionsAnswered();
        return HelperClass.generateResponse(
                "total questions answered",
                HttpStatus.OK,
                count
        );
    }
}
