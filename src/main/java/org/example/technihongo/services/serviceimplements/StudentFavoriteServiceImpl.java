package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.LearningResource;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFavorite;
import org.example.technihongo.repositories.LearningResourceRepository;
import org.example.technihongo.repositories.StudentFavoriteRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.repositories.StudentSubscriptionRepository;
import org.example.technihongo.services.interfaces.StudentFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Component
public class StudentFavoriteServiceImpl implements StudentFavoriteService {
    @Autowired
    private StudentFavoriteRepository studentFavoriteRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LearningResourceRepository learningResourceRepository;
    @Autowired
    private StudentSubscriptionRepository studentSubscriptionRepository;


    @Override
    @Transactional
    public StudentFavorite saveLearningResource(Integer studentId, Integer learningResourceId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        LearningResource learningResource = learningResourceRepository.findById(learningResourceId)
                .orElseThrow(() -> new RuntimeException("Learning resource not found with ID: " + learningResourceId));

        if (!learningResource.isPublic()) {
            throw new RuntimeException("Cannot favorite a non-public learning resource!");
        }

        if (learningResource.isPremium()) {
            boolean hasActiveSubscription = studentSubscriptionRepository
                    .existsByStudent_StudentIdAndIsActive(studentId, true);
            if (!hasActiveSubscription) {
                throw new RuntimeException("Student must have an active subscription to favorite a premium learning resource!");
            }
        }

        if (studentFavoriteRepository.existsByStudent_StudentIdAndLearningResource_ResourceId(studentId, learningResourceId)) {
            throw new RuntimeException("This learning resource has already been favorited by the student!");
        }

        StudentFavorite favorite = StudentFavorite.builder()
                .student(student)
                .learningResource(learningResource)
                .build();

        return studentFavoriteRepository.save(favorite);
    }

    @Override
    public PageResponseDTO<LearningResource> getListFavoriteLearningResourcesByStudentId(Integer studentId, int pageNo, int pageSize, String sortBy, String sortDir) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        boolean hasActiveSubscription = studentSubscriptionRepository
                .existsByStudent_StudentIdAndIsActive(studentId, true);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<StudentFavorite> favorites;
        if (hasActiveSubscription) {
            favorites = studentFavoriteRepository.findByStudent_StudentIdAndLearningResource_IsPublic(
                    studentId, true, pageable);
        } else {
            favorites = studentFavoriteRepository.findByStudent_StudentIdAndLearningResource_IsPublicAndLearningResource_IsPremium(
                    studentId, true, false, pageable);
        }

        Page<LearningResource> learningResources = favorites.map(StudentFavorite::getLearningResource);

        return getPageResponseDTO(learningResources);
    }

    private PageResponseDTO<LearningResource> getPageResponseDTO(Page<LearningResource> page) {
        return PageResponseDTO.<LearningResource>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
