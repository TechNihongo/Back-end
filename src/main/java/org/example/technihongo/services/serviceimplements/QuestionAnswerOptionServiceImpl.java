package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.Question;
import org.example.technihongo.entities.QuestionAnswerOption;
import org.example.technihongo.repositories.QuestionAnswerOptionRepository;
import org.example.technihongo.repositories.QuestionRepository;
import org.example.technihongo.services.interfaces.QuestionAnswerOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
