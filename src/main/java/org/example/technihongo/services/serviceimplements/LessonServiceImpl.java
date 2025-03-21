package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLessonDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.dto.UpdateLessonDTO;
import org.example.technihongo.dto.UpdateLessonOrderDTO;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.StudyPlan;
import org.example.technihongo.entities.Lesson;
import org.example.technihongo.repositories.StudyPlanRepository;
import org.example.technihongo.repositories.LessonRepository;
import org.example.technihongo.services.interfaces.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Override
    public Optional<Lesson> getLessonById(Integer lessonId) {
        return Optional.ofNullable(lessonRepository.findByLessonId(lessonId));
    }

    @Override
    public List<Lesson> getLessonListByStudyPlanId(Integer studyPlanId) {
        studyPlanRepository.findById(studyPlanId)
                .orElseThrow(() -> new RuntimeException("StudyPlan ID not found!"));
        return lessonRepository.findByStudyPlan_StudyPlanIdOrderByLessonOrderAsc(studyPlanId);
    }

    @Override
    public Lesson createLesson(CreateLessonDTO createLessonDTO) {
        StudyPlan csp = studyPlanRepository.findByStudyPlanId(createLessonDTO.getStudyPlanId());
        if(csp == null){
            throw new RuntimeException("StudyPlan ID not found!");
        }

        Lesson lesson = lessonRepository.save(Lesson.builder()
                .studyPlan(csp)
                .title(createLessonDTO.getTitle())
                .lessonOrder(lessonRepository.countLessonByStudyPlan(csp) + 1)
                .build());

        return lesson;
    }

    @Override
    public void updateLesson(Integer lessonId, UpdateLessonDTO updateLessonDTO) {
        if(lessonRepository.findByLessonId(lessonId) == null){
            throw new RuntimeException("Lesson ID not found!");
        }

        Lesson lesson = lessonRepository.findByLessonId(lessonId);
        lesson.setTitle(updateLessonDTO.getTitle());
        lesson.setUpdatedAt(LocalDateTime.now());

        lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public void updateLessonOrder(Integer studyPlanId, UpdateLessonOrderDTO updateLessonOrderDTO) {
        if(studyPlanRepository.findByStudyPlanId(studyPlanId) == null){
            throw new RuntimeException("StudyPlan ID not found!");
        }

        List<Lesson> lessons = lessonRepository.findByStudyPlan_StudyPlanIdOrderByLessonOrderAsc(studyPlanId);
        List<Integer> newOrder = updateLessonOrderDTO.getNewLessonOrder();

        if (lessons.size() != newOrder.size()) {
            throw new IllegalArgumentException("Lesson count does not match newOrder!");
        }

        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).setLessonOrder(newOrder.get(i));
        }

        lessonRepository.saveAll(lessons);
    }

    @Override
    public PageResponseDTO<Lesson> getLessonListByStudyPlanIdPaginated(Integer studyPlanId, int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        studyPlanRepository.findById(studyPlanId)
                .orElseThrow(() -> new RuntimeException("StudyPlan ID not found!"));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Lesson> lessons;
        if(keyword != null) {
            lessons = lessonRepository.findByStudyPlan_StudyPlanIdAndTitleContainingIgnoreCase(studyPlanId, keyword, pageable);
        }
        else {
            lessons = lessonRepository.findByStudyPlan_StudyPlanId(studyPlanId, pageable);
        }

        return getPageResponseDTO(lessons);
    }

    private PageResponseDTO<Lesson> getPageResponseDTO(Page<Lesson> page) {
        return PageResponseDTO.<Lesson>builder()
                .content(page.getContent())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
