package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateUpdateQuestionDTO;
import org.example.technihongo.entities.Question;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.services.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Question> getQuestionList() {
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestionById(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question ID not found"));
    }

    @Override
    public Question createQuestion(CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        return questionRepository.save(Question.builder()
                .questionText(createUpdateQuestionDTO.getQuestionText())
                .explanation(createUpdateQuestionDTO.getExplanation())
                .url(createUpdateQuestionDTO.getUrl())
                .build());
    }

    @Override
    public void updateQuestion(Integer questionId, CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        Question question = questionRepository.findByQuestionId(questionId);
        if(question == null){
            throw new RuntimeException("Question ID not found!");
        }

        question.setQuestionText(createUpdateQuestionDTO.getQuestionText());
        question.setExplanation(createUpdateQuestionDTO.getExplanation());
        question.setUrl(createUpdateQuestionDTO.getUrl());

        questionRepository.save(question);
    }
}
