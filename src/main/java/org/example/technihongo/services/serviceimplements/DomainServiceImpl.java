package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.DomainRequestDTO;
import org.example.technihongo.dto.DomainResponseDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.Domain;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        } else {
            domain.setParentDomain(null);
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

    @Override
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
    public PageResponseDTO<DomainResponseDTO> getAllDomains(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Domain> domainPage = domainRepository.findAll(pageable);
        List<DomainResponseDTO> content = domainPage.getContent().stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<DomainResponseDTO>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(domainPage.getTotalElements())
                .totalPages(domainPage.getTotalPages())
                .last(domainPage.isLast())
                .build();
    }

    @Override
    public PageResponseDTO<DomainResponseDTO> getAllParentDomains(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Domain> parentDomainPage = domainRepository.findByParentDomainIsNull(pageable);
        List<DomainResponseDTO> content = parentDomainPage.getContent().stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<DomainResponseDTO>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(parentDomainPage.getTotalElements())
                .totalPages(parentDomainPage.getTotalPages())
                .last(parentDomainPage.isLast())
                .build();
    }

    @Override
    public DomainResponseDTO getDomainById(Integer domainId) {
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new ResourceNotFoundException("Domain not found with ID: " + domainId));
        return convertToDomainResponseDTO(domain);
    }

    @Override
    public PageResponseDTO<DomainResponseDTO> searchName(String keyword, int pageNo, int pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return PageResponseDTO.<DomainResponseDTO>builder()
                    .content(new ArrayList<>())
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .totalElements(0)
                    .totalPages(0)
                    .last(true)
                    .build();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Domain> domainPage = domainRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        List<DomainResponseDTO> content = domainPage.getContent().stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<DomainResponseDTO>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(domainPage.getTotalElements())
                .totalPages(domainPage.getTotalPages())
                .last(domainPage.isLast())
                .build();
    }

    @Override
    public PageResponseDTO<DomainResponseDTO> getDomainsByTags(List<String> tags, int pageNo, int pageSize) {
        if (tags == null || tags.isEmpty()) {
            return PageResponseDTO.<DomainResponseDTO>builder()
                    .content(new ArrayList<>())
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .totalElements(0)
                    .totalPages(0)
                    .last(true)
                    .build();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Domain> domainPage = domainRepository.findByTagIn(tags, pageable);
        List<DomainResponseDTO> content = domainPage.getContent().stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<DomainResponseDTO>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(domainPage.getTotalElements())
                .totalPages(domainPage.getTotalPages())
                .last(domainPage.isLast())
                .build();
    }

    @Override
    public PageResponseDTO<DomainResponseDTO> getChildDomains(Integer parentDomainId, int pageNo, int pageSize) {
        Domain parentDomain = domainRepository.findById(parentDomainId)
                .orElseThrow(() -> new RuntimeException("Parent Domain not found with ID: " + parentDomainId));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Domain> childDomainPage = domainRepository.findByParentDomain(parentDomain, pageable);
        List<DomainResponseDTO> content = childDomainPage.getContent().stream()
                .map(this::convertToDomainResponseDTO)
                .collect(Collectors.toList());

        return PageResponseDTO.<DomainResponseDTO>builder()
                .content(content)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(childDomainPage.getTotalElements())
                .totalPages(childDomainPage.getTotalPages())
                .last(childDomainPage.isLast())
                .build();
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