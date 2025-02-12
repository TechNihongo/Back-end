package org.example.technihongo.repositories;

import org.example.technihongo.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson findByLessonId(Integer lessonId);
}
