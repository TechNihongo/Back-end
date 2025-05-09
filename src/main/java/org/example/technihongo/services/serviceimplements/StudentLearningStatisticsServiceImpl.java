package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.entities.StudentLearningStatistics;
import org.example.technihongo.repositories.StudentLearningStatisticsRepository;
import org.example.technihongo.services.interfaces.StudentLearningStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Component
public class StudentLearningStatisticsServiceImpl implements StudentLearningStatisticsService {
    @Autowired
    private StudentLearningStatisticsRepository studentLearningStatisticsRepository;

    @Override
    public StudentLearningStatistics viewStudentLearningStatistics(Integer studentId) {
        if(studentId == null){
            throw new RuntimeException("Student ID không thể null");
        }
        return studentLearningStatisticsRepository.findByStudentStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Student ID!"));
    }
}
