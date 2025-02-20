package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Quiz;
import org.example.technihongo.repositories.QuizRepository;
import org.example.technihongo.services.interfaces.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getQuizList() {
        return quizRepository.findAll().stream().filter(quiz -> !quiz.isDeleted()).toList();
    }

    @Override
    public List<Quiz> getPublicQuizList() {
        return quizRepository.findAll().stream().filter(quiz -> !quiz.isDeleted() && quiz.isPublic()).toList();
    }

    @Override
    public Quiz getQuizById(Integer quizId) {
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if(quiz == null || quiz.isDeleted()){
            throw new RuntimeException("Quiz ID not found!");
        }
        return quiz;
    }

    @Override
    public Quiz getPublicQuizById(Integer quizId) {
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if(quiz == null || quiz.isDeleted() || !quiz.isPublic()){
            throw new RuntimeException("Quiz ID not found!");
        }
        return quiz;
    }
}
