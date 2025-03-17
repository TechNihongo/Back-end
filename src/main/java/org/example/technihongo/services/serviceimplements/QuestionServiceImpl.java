package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.QuestionWithOptionsDTO;
import org.example.technihongo.dto.CreateUpdateQuestionDTO;
import org.example.technihongo.dto.QuestionAnswerOptionDTO;
import org.example.technihongo.dto.QuestionWithOptionsResponseDTO;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;
import org.example.technihongo.repositories.QuestionAnswerOptionRepository;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.services.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionAnswerOptionRepository questionAnswerOptionRepository;

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

    @Override
    public QuestionWithOptionsResponseDTO createQuestionWithOptions(QuestionWithOptionsDTO dto) {
        List<QuestionAnswerOptionDTO> options = dto.getOptions();

        if (options.size() < 2 || options.size() > 4) {
            throw new IllegalArgumentException("Each question must have between 2 and 4 answer options.");
        }

        long correctCount = options.stream().filter(QuestionAnswerOptionDTO::getIsCorrect).count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Each question must have exactly one correct answer.");
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
        return new QuestionWithOptionsResponseDTO(savedQuestion, optionEntities);
    }

    @Transactional
    @Override
    public QuestionWithOptionsResponseDTO updateQuestionWithOptions(Integer questionId, QuestionWithOptionsDTO dto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question ID not found."));

        question.setQuestionText(dto.getQuestionText());
        question.setExplanation(dto.getExplanation());
        question.setUrl(dto.getUrl());

        List<QuestionAnswerOption> options = updateOptions(questionId, dto.getOptions());
        Question savedQuestion = questionRepository.save(question);
        return new QuestionWithOptionsResponseDTO(savedQuestion, options);
    }

    @Transactional
    @Override
    public List<QuestionAnswerOption> updateOptions(Integer questionId, List<QuestionAnswerOptionDTO> options) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question ID not found."));

        if (options.size() < 2 || options.size() > 4) {
            throw new IllegalArgumentException("Each question must have between 2 and 4 answer options.");
        }

        long correctCount = options.stream().filter(QuestionAnswerOptionDTO::getIsCorrect).count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Each question must have exactly one correct answer.");
        }

        questionAnswerOptionRepository.deleteByQuestion_QuestionId(question.getQuestionId());

        List<QuestionAnswerOption> updatedOptions = options.stream()
                .map(opt -> QuestionAnswerOption.builder()
                        .question(question)
                        .optionText(opt.getOptionText())
                        .isCorrect(opt.getIsCorrect())
                        .build())
                .collect(Collectors.toList());

        return questionAnswerOptionRepository.saveAll(updatedOptions);
    }
}
