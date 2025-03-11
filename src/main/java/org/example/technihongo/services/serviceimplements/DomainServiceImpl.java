package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.DomainRequestDTO;
import org.example.technihongo.dto.DomainResponseDTO;
import org.example.technihongo.entities.Domain;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DomainServiceImpl implements DomainService {
    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private LearningPathRepository learningPathRepository;

    @Autowired
    private LearningResourceRepository learningResourceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SystemFlashcardSetRepository systemFlashcardSetRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public DomainResponseDTO createDomain(DomainRequestDTO request) {
        Domain domain = new Domain();
        domain.setTag(request.getTag());
        domain.setName(request.getName());
        domain.setDescription(request.getDescription());

        if (request.getParentDomainId() != null) {
            Domain parentDomain = domainRepository.findById(request.getParentDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Domain not found with ID: " + request.getParentDomainId()));
            domain.setParentDomain(parentDomain);
        }

        Domain savedDomain = domainRepository.save(domain);
        return convertToDomainResponseDTO(savedDomain);
    }

    @Override
    public DomainResponseDTO updateDomain(Integer domainId, DomainRequestDTO request) {
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with ID: " + domainId));

        if (request.getTag() != null) {
            domain.setTag(request.getTag());
        }
        if (request.getName() != null) {
            domain.setName(request.getName());
        }
        if (request.getDescription() != null) {
            domain.setDescription(request.getDescription());
        }
        if (request.getParentDomainId() != null) {
            Domain parentDomain = domainRepository.findById(request.getParentDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Domain not found with ID: " + request.getParentDomainId()));
            domain.setParentDomain(parentDomain);
        }

        Domain updatedDomain = domainRepository.save(domain);
        return convertToDomainResponseDTO(updatedDomain);
    }


    public void deleteDomain(Integer domainId) {
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with ID: " + domainId));

        if (domain.getSubDomains() != null && !domain.getSubDomains().isEmpty()) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it has associated child domains.");
        }

        if (learningPathRepository.existsByDomainDomainId(domainId)) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it is referenced by one or more learning paths.");
        }

        if (learningResourceRepository.existsByDomainDomainId(domainId)) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it is referenced by one or more learning resources.");
        }

        if (courseRepository.existsByDomainDomainId(domainId)) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it is referenced by one or more courses.");
        }

        if (systemFlashcardSetRepository.existsByDomainDomainId(domainId)) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it is referenced by one or more flashcard sets.");
        }

        if (quizRepository.existsByDomainDomainId(domainId)) {
            throw new RuntimeException("Cannot delete domain with ID: " + domainId +
                    " because it is referenced by one or more quizzes.");
        }

        domainRepository.delete(domain);
    }

    @Override
    public List<DomainResponseDTO> getAllDomains() {
        List<Domain> domains = domainRepository.findAll();
        return domains.stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DomainResponseDTO getDomainById(Integer domainId) {
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with ID: " + domainId));
        return convertToDomainResponseDTO(domain);
    }

    @Override
    public List<DomainResponseDTO> searchName(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Domain> domains = domainRepository.findByNameContainingIgnoreCase(keyword.trim());
        return domains.stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());
    }


//    @Override
//    public boolean hasCourses(Integer domainId) {
//        Domain domain = domainRepository.findById(domainId)
//                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with ID: " + domainId));
//
//        List<Course> courses = courseRepository.findByDomainId(domainId);
//
//        if (!courses.isEmpty()) {
//            System.out.println("Domain " + domain.getName() + " has following courses:");
//            courses.forEach(course -> System.out.println("- " + course.getTitle()));
//        }
//
//        return !courses.isEmpty();
//    }

    @Override
    public List<DomainResponseDTO> getDomainsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        List<Domain> domains = domainRepository.findByTagIn(tags);
        return domains.stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DomainResponseDTO> getChildDomains(Integer parentDomainId) {
        Domain parentDomain = domainRepository.findById(parentDomainId)
                .orElseThrow(() -> new RuntimeException("Parent Domain not found with ID: " + parentDomainId));

        List<Domain> childDomains = domainRepository.findByParentDomain(parentDomain);
        return childDomains.stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());
    }

    private DomainResponseDTO convertToDomainResponseDTO(Domain domain) {
        DomainResponseDTO response = new DomainResponseDTO();
        response.setDomainId(domain.getDomainId());
        response.setTag(domain.getTag());
        response.setName(domain.getName());
        response.setDescription(domain.getDescription());
        response.setParentDomainId(domain.getParentDomain() != null ? domain.getParentDomain().getDomainId() : null);
        return response;
    }
}
