package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateQuizQuestionDTO;
import org.example.technihongo.dto.UpdateQuizQuestionOrderDTO;
import org.example.technihongo.entities.QuizQuestion;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.repositories.QuizQuestionRepository;
import org.example.technihongo.repositories.QuizRepository;
import org.example.technihongo.services.interfaces.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class QuizQuestionServiceImpl implements QuizQuestionService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Override
    public List<QuizQuestion> getQuizQuestionsByQuizId(Integer quizId) {
        if(quizRepository.findByQuizId(quizId) == null){
            throw new RuntimeException("Quiz ID not found!");
        }
        return quizQuestionRepository.findByQuiz_QuizIdOrderByQuestionOrderAsc(quizId);
    }

    @Override
    public QuizQuestion getQuizQuestionById(Integer quizQuestionId) {
        return quizQuestionRepository.findById(quizQuestionId)
                .orElseThrow(() -> new RuntimeException("QuizQuestion ID not found"));
    }

    @Override
    public QuizQuestion createQuizQuestion(CreateQuizQuestionDTO createQuizQuestionDTO) {
        if(quizRepository.findByQuizId(createQuizQuestionDTO.getQuizId()) == null){
            throw new RuntimeException("Quiz ID not found!");
        }

        if(questionRepository.findByQuestionId(createQuizQuestionDTO.getQuestionId()) == null){
            throw new RuntimeException("Question ID not found!");
        }

        QuizQuestion quizQuestion = quizQuestionRepository.save(QuizQuestion.builder()
                .quiz(quizRepository.findByQuizId(createQuizQuestionDTO.getQuizId()))
                .question(questionRepository.findByQuestionId(createQuizQuestionDTO.getQuestionId()))
                .questionOrder(quizQuestionRepository.countByQuiz_QuizId(createQuizQuestionDTO.getQuizId()) + 1)
                .build());

        return quizQuestion;
    }

    @Override
    public void updateQuizQuestionOrder(Integer quizId, UpdateQuizQuestionOrderDTO updateQuizQuestionOrderDTO) {
        if(quizRepository.findByQuizId(quizId) == null){
            throw new RuntimeException("Quiz ID not found!");
        }

        List<QuizQuestion> quizQuestions = quizQuestionRepository.findByQuiz_QuizIdOrderByQuestionOrderAsc(quizId);
        List<Integer> newOrder = updateQuizQuestionOrderDTO.getNewQuizQuestionOrder();

        if (quizQuestions.size() != newOrder.size()) {
            throw new IllegalArgumentException("QuizQuestion count does not match newOrder!");
        }

        for (int i = 0; i < quizQuestions.size(); i++) {
            quizQuestions.get(i).setQuestionOrder(newOrder.get(i));
        }

        quizQuestionRepository.saveAll(quizQuestions);
    }

    @Override
    public void deleteQuizQuestion(Integer quizQuestionId) {
        QuizQuestion deletedQuizQuestion = quizQuestionRepository.findById(quizQuestionId)
                .orElseThrow(() -> new RuntimeException("QuizQuestion ID not found"));

        Integer quizId = deletedQuizQuestion.getQuiz().getQuizId();
        Integer deletedOrder = deletedQuizQuestion.getQuestionOrder();

        quizQuestionRepository.delete(deletedQuizQuestion);

        List<QuizQuestion> quizQuestions = quizQuestionRepository.findByQuiz_QuizIdOrderByQuestionOrderAsc(quizId);
        for (QuizQuestion quizQuestion : quizQuestions) {
            if (quizQuestion.getQuestionOrder() > deletedOrder) {
                quizQuestion.setQuestionOrder(quizQuestion.getQuestionOrder() - 1);
            }
        }

        quizQuestionRepository.saveAll(quizQuestions);
    }
}
