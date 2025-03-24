package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.*;
import org.example.technihongo.repositories.QuestionAnswerOptionRepository;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.repositories.QuizQuestionRepository;
import org.example.technihongo.repositories.QuizRepository;
import org.example.technihongo.services.interfaces.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private QuestionAnswerOptionRepository questionAnswerOptionRepository;

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

    @Override
    public void setQuizQuestionOrder(Integer quizId, Integer quizQuestionId, Integer newOrder) {
        quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz ID not found"));

        quizQuestionRepository.findById(quizQuestionId)
                .orElseThrow(() -> new RuntimeException("QuizQuestion ID not found"));

        List<QuizQuestion> quizQuestions = quizQuestionRepository.findByQuiz_QuizIdOrderByQuestionOrderAsc(quizId);
        QuizQuestion target = quizQuestions.stream()
                .filter(qq -> qq.getQuizQuestionId().equals(quizQuestionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("QuizQuestion not found!"));

        int currentOrder = target.getQuestionOrder();
        if (newOrder < 1 || newOrder > quizQuestions.size()) {
            throw new RuntimeException("Invalid order!");
        }

        if (newOrder < currentOrder) {
            quizQuestions.stream()
                    .filter(qq -> qq.getQuestionOrder() >= newOrder && qq.getQuestionOrder() < currentOrder)
                    .forEach(qq -> qq.setQuestionOrder(qq.getQuestionOrder() + 1));
        } else if (newOrder > currentOrder) {
            quizQuestions.stream()
                    .filter(qq -> qq.getQuestionOrder() <= newOrder && qq.getQuestionOrder() > currentOrder)
                    .forEach(qq -> qq.setQuestionOrder(qq.getQuestionOrder() - 1));
        }
        target.setQuestionOrder(newOrder);

        quizQuestionRepository.saveAll(quizQuestions);
    }

    @Override
    public QuizQuestionWithNewQuestionResponseDTO createQuizQuestionWithNewQuestion(CreateQuizQuestionWithNewQuestionDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz ID not found!"));

        List<QuestionAnswerOptionDTO> options = dto.getOptions();
        if (options.size() < 2 || options.size() > 4) {
            throw new RuntimeException("Each question must have between 2 and 4 answer options.");
        }
        long correctCount = options.stream().filter(QuestionAnswerOptionDTO::getIsCorrect).count();
        if (correctCount != 1) {
            throw new RuntimeException("Each question must have exactly one correct answer.");
        }

        Question question = Question.builder()
                .questionText(dto.getQuestionText())
                .explanation(dto.getExplanation())
                .url(dto.getUrl())
                .build();
        Question savedQuestion = questionRepository.save(question);

        List<QuestionAnswerOption> optionEntities = options.stream()
                .map(opt -> QuestionAnswerOption.builder()
                        .question(savedQuestion)
                        .optionText(opt.getOptionText())
                        .isCorrect(opt.getIsCorrect())
                        .build())
                .collect(Collectors.toList());
        questionAnswerOptionRepository.saveAll(optionEntities);

        QuizQuestion quizQuestion = QuizQuestion.builder()
                .quiz(quiz)
                .question(savedQuestion)
                .questionOrder(quizQuestionRepository.countByQuiz_QuizId(dto.getQuizId()) + 1)
                .build();
        QuizQuestion savedQuizQuestion = quizQuestionRepository.save(quizQuestion);

        return QuizQuestionWithNewQuestionResponseDTO.builder()
                .quizQuestion(savedQuizQuestion)
                .options(optionEntities)
                .build();
    }
}
