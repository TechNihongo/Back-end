package org.example.technihongo.repositories;

import org.example.technihongo.entities.StudentFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentFolderRepository extends JpaRepository<StudentFolder, Integer> {
    List<StudentFolder> findByStudentStudentId(Integer studentId);
}
