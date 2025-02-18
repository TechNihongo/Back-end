package org.example.technihongo.repositories;

import org.example.technihongo.entities.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonResourceRepository extends JpaRepository<LessonResource, Integer> {
    void deleteByLesson_LessonIdIn(List<Integer> lessonIds);
}
