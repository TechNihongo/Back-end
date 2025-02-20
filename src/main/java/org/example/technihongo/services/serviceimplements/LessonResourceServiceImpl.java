package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.CreateLessonResourceDTO;
import org.example.technihongo.dto.UpdateLessonResourceDTO;
import org.example.technihongo.dto.UpdateLessonResourceOrderDTO;
import org.example.technihongo.entities.LessonResource;
import org.example.technihongo.repositories.LessonRepository;
import org.example.technihongo.repositories.LessonResourceRepository;
import org.example.technihongo.services.interfaces.LessonResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class LessonResourceServiceImpl implements LessonResourceService {
    @Autowired
    private LessonResourceRepository lessonResourceRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<LessonResource> getLessonResourceListByLessonId(Integer lessonId) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        return lessonResourceRepository.findAll().stream()
                .filter(q -> q.getLesson().getLessonId().equals(lessonId))
                .toList();
    }

    @Override
    public List<LessonResource> getActiveLessonResourceListByLessonId(Integer lessonId) {
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson ID not found"));

        return lessonResourceRepository.findAll().stream()
                .filter(q -> q.getLesson().getLessonId().equals(lessonId) && q.isActive())
                .toList();
    }

    @Override
    public LessonResource getLessonResourceById(Integer lessonResourceId) {
        return lessonResourceRepository.findById(lessonResourceId)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));
    }

    @Override
    public LessonResource getActiveLessonResourceById(Integer lessonResourceId) {
        return lessonResourceRepository.findById(lessonResourceId)
                .filter(LessonResource::isActive)
                .orElseThrow(() -> new RuntimeException("LessonResource ID not found"));    }

    @Override
    public LessonResource createLessonResource(CreateLessonResourceDTO createLessonResourceDTO) {
        return null;
    }

    @Override
    public void updateLessonResource(Integer lessonResourceId, UpdateLessonResourceDTO updateLessonResourceDTO) {

    }

    @Override
    public void updateLessonResourceOrder(Integer lessonResourceId, UpdateLessonResourceOrderDTO updateLessonResourceOrderDTO) {

    }

    @Override
    public void deleteLessonResource(Integer lessonResourceId) {

    }
}
