package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.QuestionAnswerOptionDTO;
import org.example.technihongo.dto.QuestionAnswerOptionListDTO;
import org.example.technihongo.dto.QuestionWithOptionsRespondDTO;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;
import org.example.technihongo.repositories.QuestionAnswerOptionRepository;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.services.interfaces.QuestionAnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class QuestionAnswerOptionServiceImpl implements QuestionAnswerOptionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionAnswerOptionRepository questionAnswerOptionRepository;

    @Override
    public List<QuestionAnswerOption> getOptionListByQuestionId(Integer questionId) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question ID not found"));

        return questionAnswerOptionRepository.findAll().stream()
                .filter(q -> q.getQuestion().getQuestionId().equals(questionId))
                .toList();
    }

    @Override
    public QuestionAnswerOption getOptionById(Integer optionId) {
        return questionAnswerOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option ID not found"));
    }

    @Override
    public QuestionWithOptionsRespondDTO createAnswerOptionList(QuestionAnswerOptionListDTO dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question ID not found!"));

        List<QuestionAnswerOptionDTO> options = dto.getOptions();

        if (options.size() < 2 || options.size() > 4) {
            throw new IllegalArgumentException("Each question must have between 2 and 4 answer options.");
        }

        long correctCount = options.stream().filter(QuestionAnswerOptionDTO::getIsCorrect).count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Each question must have exactly one correct answer.");
        }

        List<QuestionAnswerOption> optionEntities = options.stream()
                .map(opt -> QuestionAnswerOption.builder()
                        .question(question)
                        .optionText(opt.getOptionText())
                        .isCorrect(opt.getIsCorrect())
                        .build())
                .collect(Collectors.toList());

        questionAnswerOptionRepository.saveAll(optionEntities);
        return new QuestionWithOptionsRespondDTO(question, optionEntities);
    }

    @Transactional
    @Override
    public QuestionWithOptionsRespondDTO updateAnswerOptionList(QuestionAnswerOptionListDTO dto) {
        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question ID not found."));

        List<QuestionAnswerOptionDTO> options = dto.getOptions();
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

        questionAnswerOptionRepository.saveAll(updatedOptions);
        return new QuestionWithOptionsRespondDTO(question, updatedOptions);
    }
}
