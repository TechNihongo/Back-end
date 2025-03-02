package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLearningPathDTO;
import org.example.technihongo.dto.UpdateLearningPathDTO;
import org.example.technihongo.entities.LearningPath;
import org.example.technihongo.entities.PathCourse;
import org.example.technihongo.repositories.DomainRepository;
import org.example.technihongo.repositories.LearningPathRepository;
import org.example.technihongo.repositories.PathCourseRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.LearningPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class LearningPathServiceImpl implements LearningPathService {
    @Autowired
    private LearningPathRepository learningPathRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private PathCourseRepository pathCourseRepository;

    @Override
    public List<LearningPath> getAllLearningPaths() {
        return learningPathRepository.findAll();
    }

    @Override
    public List<LearningPath> getPublicLearningPaths() {
        return learningPathRepository.findAll().stream().filter(LearningPath::isPublic).toList();
    }

    @Override
    public List<LearningPath> getLearningPathsByTitle(String keyword) {
        return learningPathRepository.findByTitleContainsIgnoreCase(keyword)
                .stream().filter(LearningPath::isPublic).toList();
    }

    @Override
    public LearningPath getLearningPathById(Integer pathId) {
        return learningPathRepository.findByPathId(pathId);
    }

    @Override
    public LearningPath getPublicLearningPathById(Integer pathId) {
        LearningPath learningPath = learningPathRepository.findByPathId(pathId);
        if(learningPath != null && learningPath.isPublic()) {
            return learningPath;
        }
        else return null;
    }

    @Override
    public LearningPath createLearningPath(Integer creatorId, CreateLearningPathDTO createLearningPathDTO) {
        if(userRepository.findByUserId(creatorId) == null){
            throw new RuntimeException("Creator ID not found!");
        }

        if(domainRepository.findByDomainId(createLearningPathDTO.getDomainId()) == null){
            throw new RuntimeException("Domain ID not found!");
        }

        LearningPath learningPath = learningPathRepository.save(LearningPath.builder()
                .title(createLearningPathDTO.getTitle())
                .description(createLearningPathDTO.getDescription())
                .domain(domainRepository.findByDomainId(createLearningPathDTO.getDomainId()))
                .creator(userRepository.findByUserId(creatorId))
                .totalCourses(0)
                .build());

        return learningPath;
    }

    @Override
    public void updateLearningPath(Integer pathId, UpdateLearningPathDTO updateLearningPathDTO) {
        if(learningPathRepository.findByPathId(pathId) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }

        if(domainRepository.findByDomainId(updateLearningPathDTO.getDomainId()) == null){
            throw new RuntimeException("Domain ID not found!");
        }

        LearningPath learningPath = learningPathRepository.findByPathId(pathId);
        learningPath.setTitle(updateLearningPathDTO.getTitle());
        learningPath.setDescription(updateLearningPathDTO.getDescription());
        learningPath.setDomain(domainRepository.findByDomainId(updateLearningPathDTO.getDomainId()));
        learningPath.setPublic(updateLearningPathDTO.getIsPublic());

        learningPathRepository.save(learningPath);
    }

    @Override
    @Transactional
    public void deleteLearningPath(Integer pathId) {
        if(learningPathRepository.findByPathId(pathId) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }

        LearningPath learningPath = learningPathRepository.findByPathId(pathId);
        if(learningPath.isPublic()){
            throw new RuntimeException("Cannot delete an active learning path!");
        }

        List<PathCourse> pathCourses = pathCourseRepository.findByLearningPath_PathId(pathId);
        pathCourseRepository.deleteAll(pathCourses);
        learningPathRepository.delete(learningPath);
    }

    @Override
    public void updateTotalCourses(Integer pathId) {
        if(learningPathRepository.findByPathId(pathId) == null){
            throw new RuntimeException("LearningPath ID not found!");
        }

        LearningPath learningPath = learningPathRepository.findByPathId(pathId);
        learningPath.setTotalCourses(pathCourseRepository.countByLearningPath_PathId(pathId));
        learningPathRepository.save(learningPath);
    }

    @Override
    public List<LearningPath> getListLearningPathsByCreatorId(Integer creatorId) {
        userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User ID not found."));
        return learningPathRepository.findByCreator_UserId(creatorId);
    }
}
