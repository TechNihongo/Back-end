package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateQuizDTO;
import org.example.technihongo.dto.UpdateQuizDTO;
import org.example.technihongo.dto.UpdateQuizStatusDTO;
import org.example.technihongo.entities.*;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

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

    @Override
    public Quiz createQuiz(Integer creatorId, CreateQuizDTO createQuizDTO) {
        User user = userRepository.findById(creatorId).orElseThrow(()
                -> new RuntimeException("User ID not found!"));

        Domain domain = domainRepository.findById(createQuizDTO.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain ID not found!"));

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(createQuizDTO.getDifficultyLevelId())
                .orElseThrow(() -> new RuntimeException("DifficultyLevel ID not found!"));

        if(createQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(1)) > 0
                || createQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new RuntimeException("PassingScore must between 0 and 1!");
        }

        Quiz quiz = quizRepository.save(Quiz.builder()
                        .title(createQuizDTO.getTitle())
                        .description(createQuizDTO.getDescription())
                        .domain(domain)
                        .creator(user)
                        .difficultyLevel(difficultyLevel)
                        .totalQuestions(0)
                        .passingScore(createQuizDTO.getPassingScore())
                .build());

        return quiz;
    }

    @Override
    public void updateQuiz(Integer quizId, UpdateQuizDTO updateQuizDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz ID not found!"));

        if(quiz.isPublic()){
            throw new RuntimeException("Cannot update a public quiz");
        }

        Domain domain = domainRepository.findById(updateQuizDTO.getDomainId())
                .orElseThrow(() -> new RuntimeException("Domain ID not found!"));

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(updateQuizDTO.getDifficultyLevelId())
                .orElseThrow(() -> new RuntimeException("DifficultyLevel ID not found!"));

        if(updateQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(1)) > 0
                || updateQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new RuntimeException("PassingScore must between 0 and 1!");
        }

        quiz.setTitle(updateQuizDTO.getTitle());
        quiz.setDescription(updateQuizDTO.getDescription());
        quiz.setDomain(domain);
        quiz.setDifficultyLevel(difficultyLevel);
        quiz.setPassingScore(updateQuizDTO.getPassingScore());

        quizRepository.save(quiz);
    }

    @Override
    public void updateQuizStatus(Integer quizId, UpdateQuizStatusDTO updateQuizStatusDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz ID not found!"));

        if(updateQuizStatusDTO.getIsPublic() && updateQuizStatusDTO.getIsDeleted()){
            throw new RuntimeException("Cannot delete a public quiz");
        }

        quiz.setPublic(updateQuizStatusDTO.getIsPublic());
        quiz.setDeleted(updateQuizStatusDTO.getIsDeleted());

        quizRepository.save(quiz);
    }

    @Override
    public void updateTotalQuestions(Integer quizId) {
        if(quizRepository.findByQuizId(quizId) == null){
            throw new RuntimeException("Quiz ID not found!");
        }

        Quiz quiz = quizRepository.findByQuizId(quizId);
        quiz.setTotalQuestions(quizQuestionRepository.countByQuiz_QuizId(quizId));
        quizRepository.save(quiz);
    }
}
