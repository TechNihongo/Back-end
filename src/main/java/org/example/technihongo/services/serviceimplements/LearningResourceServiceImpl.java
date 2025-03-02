package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.LearningResourceDTO;
import org.example.technihongo.dto.LearningResourceStatusDTO;
import org.example.technihongo.entities.Domain;
import org.example.technihongo.entities.LearningResource;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.User;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.LearningResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class LearningResourceServiceImpl implements LearningResourceService {
    @Autowired
    private LearningResourceRepository learningResourceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentSubscriptionRepository studentSubscriptionRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private StudentFavoriteRepository studentFavoriteRepository;
    @Autowired
    private StudentResourceProgressRepository studentResourceProgressRepository;
    @Autowired
    private StudentFlashcardSetRepository studentFlashcardSetRepository;
    @Autowired
    private LessonResourceRepository lessonResourceRepository;


    @Override
    public List<LearningResource> getAllLearningResources() {
        return learningResourceRepository.findAll();
    }

    @Override
    public LearningResource getLearningResourceById(Integer learningResourceId) {
        return learningResourceRepository.findById(learningResourceId)
                .orElseThrow(() -> new RuntimeException("Learning resource ID not found!"));
    }

    @Override
    public LearningResource getPublicLearningResourceById(Integer userId, Integer learningResourceId) {
        LearningResource resource = learningResourceRepository.findByResourceId(learningResourceId);
        if(resource == null || !resource.isPublic()) {
            throw new RuntimeException("Learning resource ID not found!");
        }

        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new RuntimeException("User ID not found!");
        }

        if(user.getRole().getRoleId() == 3){
            Student student = studentRepository.findByUser_UserId(user.getUserId());
            if(student == null){
                throw new RuntimeException("Student not found for this user!");
            }

            if(!studentSubscriptionRepository.existsByStudent_StudentIdAndIsActive(student.getStudentId(), true)){
                throw new RuntimeException("Student not allowed to view this learning resource!");
            }
        }

        return resource;
    }

    @Override
    public LearningResource createLearningResource(Integer creatorId, LearningResourceDTO learningResourceDTO) {
        User user = userRepository.findByUserId(creatorId);
        if(user == null){
            throw new RuntimeException("User ID not found!");
        }

        Domain domain = domainRepository.findByDomainId(learningResourceDTO.getDomainId());
        if(domain == null){
            throw new RuntimeException("Domain ID not found!");
        }
        if(domain.getParentDomain() == null){
            throw new RuntimeException("Cannot assign parent domain!");
        }

        LearningResource resource = learningResourceRepository.save(LearningResource.builder()
                .title(learningResourceDTO.getTitle())
                .description(learningResourceDTO.getDescription())
                .domain(domain)
                .creator(user)
                .videoUrl(learningResourceDTO.getVideoUrl())
                .videoFilename(learningResourceDTO.getVideoFilename())
                .pdfUrl(learningResourceDTO.getPdfUrl())
                .pdfFilename(learningResourceDTO.getPdfFilename())
                .build());

        return resource;
    }

    @Override
    public void updateLearningResource(Integer learningResourceId, LearningResourceDTO learningResourceDTO) {
        LearningResource resource = learningResourceRepository.findByResourceId(learningResourceId);
        if(resource == null) {
            throw new RuntimeException("Learning resource ID not found!");
        }

        Domain domain = domainRepository.findByDomainId(learningResourceDTO.getDomainId());
        if(domain == null){
            throw new RuntimeException("Domain ID not found!");
        }
        if(domain.getParentDomain() == null){
            throw new RuntimeException("Cannot assign parent domain!");
        }

        if(resource.isPublic()){
            throw new RuntimeException("Cannot update a public Learning resource!");
        }

        resource.setTitle(learningResourceDTO.getTitle());
        resource.setDescription(learningResourceDTO.getDescription());
        resource.setDomain(domain);
        resource.setVideoUrl(learningResourceDTO.getVideoUrl());
        resource.setVideoFilename(learningResourceDTO.getVideoFilename());
        resource.setPdfUrl(learningResourceDTO.getPdfUrl());
        resource.setPdfFilename(learningResourceDTO.getPdfFilename());
        learningResourceRepository.save(resource);
    }

    @Override
    public void updateLearningResourceStatus(Integer learningResourceId, LearningResourceStatusDTO learningResourceStatusDTO) {
        LearningResource resource = learningResourceRepository.findByResourceId(learningResourceId);
        if(resource == null) {
            throw new RuntimeException("Learning resource ID not found!");
        }

        resource.setPremium(learningResourceStatusDTO.getIsPremium());
        resource.setPublic(learningResourceStatusDTO.getIsPublic());
        learningResourceRepository.save(resource);
    }

    @Override
    public void deleteLearningResource(Integer learningResourceId) {
        LearningResource resource = learningResourceRepository.findByResourceId(learningResourceId);
        if(resource == null) {
            throw new RuntimeException("Learning resource ID not found!");
        }

        if(resource.isPublic()){
            throw new RuntimeException("Cannot delete a public Learning resource!");
        }

        if (studentFavoriteRepository.existsByLearningResource_ResourceId(learningResourceId) ||
            studentFlashcardSetRepository.existsByLearningResource_ResourceId(learningResourceId) ||
            lessonResourceRepository.existsByLearningResource_ResourceId(learningResourceId) ||
            studentResourceProgressRepository.existsByLearningResource_ResourceId(learningResourceId)){
                throw new RuntimeException("Cannot delete a referenced Learning resource!");
        }

        learningResourceRepository.delete(resource);
    }

    @Override
    public List<LearningResource> getListLearningResourcesByCreatorId(Integer creatorId) {
        userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User ID not found."));
        return learningResourceRepository.findByCreator_UserId(creatorId);
    }
}
