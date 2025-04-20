package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.*;
import org.example.technihongo.entities.*;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Component
@Transactional
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentSubscriptionRepository studentSubscriptionRepository;
    @Autowired
    private LessonResourceRepository lessonResourceRepository;
    @Autowired
    private StudentQuizAttemptRepository studentQuizAttemptRepository;


    @Override
    public List<Quiz> getQuizList() {
        return quizRepository.findAll().stream().filter(quiz -> !quiz.isDeleted()).toList();
    }

    @Override
    public List<Quiz> getPublicQuizList() {
        return quizRepository.findAll().stream().filter(quiz -> !quiz.isDeleted() && quiz.isPublic()).toList();
    }

    @Override
    public QuizDTO getQuizById(Integer quizId) {
        if(quizId == null){
            throw new RuntimeException("Quiz ID không được null");
        }
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if(quiz == null || quiz.isDeleted()){
            throw new RuntimeException("Không tìm thấy Quiz!");
        }
        return convertToDTO(quiz);
    }

    @Override
    public QuizDTO getPublicQuizById(Integer userId, Integer quizId) {
        if(quizId == null){
            throw new RuntimeException("Quiz ID không thể null");
        }
        Quiz quiz = quizRepository.findByQuizId(quizId);
        if(quiz == null || quiz.isDeleted() || !quiz.isPublic()){
            throw new RuntimeException("Không tìm thấy Quiz!");
        }

        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new RuntimeException("Không tìm thấy User!");
        }

        if(user.getRole().getRoleId() == 3){
            Student student = studentRepository.findByUser_UserId(user.getUserId());
            if(student == null){
                throw new RuntimeException("Không tìm thấy Student cho User này!");
            }

            if(!studentSubscriptionRepository.existsByStudent_StudentIdAndIsActive(student.getStudentId(), true)){
                throw new RuntimeException("Student không có quyền để xem Quiz này.");
            }
        }
        return convertToDTO(quiz);
    }

    @Override
    public Quiz createQuiz(Integer creatorId, CreateQuizDTO createQuizDTO) {
        User user = userRepository.findById(creatorId).orElseThrow(()
                -> new RuntimeException("Không tìm thấy User!"));

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(createQuizDTO.getDifficultyLevelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DifficultyLevel!"));

        if(createQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(1)) > 0
                || createQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new RuntimeException("PassingScore phải nằm trong khoảng từ 0,00 đến 1,00!");
        }

        if(createQuizDTO.getTimeLimit() < 10 || createQuizDTO.getTimeLimit() > 120){
            throw new RuntimeException("TimeLimit phải nằm trong khoảng từ 10 đến 120 phút!");
        }

        Quiz quiz = quizRepository.save(Quiz.builder()
                .title(createQuizDTO.getTitle())
                .description(createQuizDTO.getDescription())
                .creator(user)
                .difficultyLevel(difficultyLevel)
                .totalQuestions(0)
                .passingScore(createQuizDTO.getPassingScore())
                .timeLimit(createQuizDTO.getTimeLimit())
                .build());

        return quiz;
    }

    @Override
    public void updateQuiz(Integer quizId, UpdateQuizDTO updateQuizDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Quiz!"));

        if(quiz.isPublic()){
            throw new RuntimeException("Không thể cập nhật Quiz công khai.");
        }

        if(studentQuizAttemptRepository.existsByQuiz_QuizId(quizId)){
            throw new RuntimeException("Không thể cập nhật Quiz đang trong quá trình học.");
        }

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findById(updateQuizDTO.getDifficultyLevelId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DifficultyLevel!"));

        if(updateQuizDTO.getTimeLimit() < 10 || updateQuizDTO.getTimeLimit() > 120){
            throw new RuntimeException("TimeLimit phải nằm trong khoảng từ 10 đến 120 phút!");
        }

        if(updateQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(1)) > 0
                || updateQuizDTO.getPassingScore().compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new RuntimeException("PassingScore phải nằm trong khoảng từ 0,00 đến 1,00!");
        }

        quiz.setTitle(updateQuizDTO.getTitle());
        quiz.setDescription(updateQuizDTO.getDescription());
        quiz.setDifficultyLevel(difficultyLevel);
        quiz.setPassingScore(updateQuizDTO.getPassingScore());
        quiz.setTimeLimit(updateQuizDTO.getTimeLimit());

        quizRepository.save(quiz);
    }

    @Override
    public void updateQuizStatus(Integer quizId, UpdateQuizStatusDTO updateQuizStatusDTO) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Quiz!"));

        if(updateQuizStatusDTO.getIsPublic() && updateQuizStatusDTO.getIsDeleted()){
            throw new RuntimeException("Không thể xóa Quiz công khai.");
        }

        quiz.setPublic(updateQuizStatusDTO.getIsPublic());
        quiz.setDeleted(updateQuizStatusDTO.getIsDeleted());

        quizRepository.save(quiz);

        List<LessonResource> lessonResources = lessonResourceRepository.findByQuiz_QuizId(quizId);
        for (LessonResource lessonResource : lessonResources) {
            lessonResource.setActive(updateQuizStatusDTO.getIsPublic());
            lessonResourceRepository.save(lessonResource);
        }
    }

    @Override
    public void updateTotalQuestions(Integer quizId) {
        if(quizRepository.findByQuizId(quizId) == null){
            throw new RuntimeException("Không tìm thấy Quiz!");
        }

        Quiz quiz = quizRepository.findByQuizId(quizId);
        quiz.setTotalQuestions(quizQuestionRepository.countByQuiz_QuizId(quizId));
        quizRepository.save(quiz);
    }

    @Override
    public List<Quiz> getListQuizzesByCreatorId(Integer creatorId) {
        userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User!"));
        return quizRepository.findByCreator_UserId(creatorId);
    }

    private QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setQuizId(quiz.getQuizId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setCreator(quiz.getCreator());
        quizDTO.setDifficultyLevel(quiz.getDifficultyLevel());
        quizDTO.setTotalQuestions(quiz.getTotalQuestions());
        quizDTO.setPassingScore(quiz.getPassingScore());
        quizDTO.setTimeLimit(quiz.getTimeLimit());
        quizDTO.setPublic(quiz.isPublic());
        quizDTO.setDeleted(quiz.isDeleted());
        quizDTO.setPremium(quiz.isPremium());
        quizDTO.setCreatedAt(quiz.getCreatedAt());
        quizDTO.setUpdatedAt(quiz.getUpdatedAt());
        quizDTO.setHasAttempt(studentQuizAttemptRepository.existsByQuiz_QuizId(quiz.getQuizId()));
        return quizDTO;
    }
}
