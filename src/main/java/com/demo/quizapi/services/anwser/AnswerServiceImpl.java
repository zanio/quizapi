package com.demo.quizapi.services.anwser;

import com.demo.quizapi.config.context.SecurityContextConfig;
import com.demo.quizapi.dao.AnswerDao;
import com.demo.quizapi.entities.Answer;
import com.demo.quizapi.entities.Option;
import com.demo.quizapi.entities.User;
import com.demo.quizapi.exceptions.custom.EntityAlreadyExistException;
import com.demo.quizapi.exceptions.custom.EntityNotFoundException;
import com.demo.quizapi.repository.AnswerRepository;
import com.demo.quizapi.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AnswerServiceImpl implements AnswerService{
    @Autowired
    private AnswerRepository answerRepositoryImpl;
    @Autowired
    private OptionRepository optionRepositoryImpl;
    @Autowired
    private SecurityContextConfig securityContextConfig;
    @Override
    public Answer create(AnswerDao answerDao) {
        Answer answer = new Answer();
        User userInDb = securityContextConfig.getUserExistInSecurityContext();
        Option option = findOneOption(answerDao.getOptionId());
       Answer answerExist = answerRepositoryImpl.findByUserAndOption(userInDb,option);
       if(answerExist!=null){
           throw new EntityAlreadyExistException(Answer.class,"answer",answerExist.getId().toString());
       }
        answer.setOption(option);
        answer.setUser(userInDb);
        return answerRepositoryImpl.save(answer);
    }

    @Override
    public Answer update(Integer answerId, Integer optionId) {
       Answer answer = findOne(answerId);
        Option option = findOneOption(optionId);
        answer.setOption(option);
        return answerRepositoryImpl.save(answer);
    }

    private Option findOneOption(Integer optionId) {
        return optionRepositoryImpl.findById(optionId).orElseThrow(
                () -> new EntityNotFoundException(Option.class, "Id", optionId.toString())
        );
    }

    @Override
    public Answer findOne(Integer answerId) {
        return answerRepositoryImpl.findById(answerId).orElseThrow(
                () -> new EntityNotFoundException(Answer.class, "Id", answerId.toString())
        );
    }

    @Override
    public Set<Answer> getAllCorrectAnswer() {
        User userInDb = securityContextConfig.getUserExistInSecurityContext();
        return answerRepositoryImpl.findByOption_CorrectAndUser(true,userInDb);
    }

    @Override
    public Set<Answer> getAllWrongAnswer() {
       User userInDb = securityContextConfig.getUserExistInSecurityContext();
        return answerRepositoryImpl.findByOption_CorrectAndUser(false,userInDb);
    }

    @Override
    public Long totalNumberOfQuestionsAnswered() {
        return answerRepositoryImpl.count();
    }
}
