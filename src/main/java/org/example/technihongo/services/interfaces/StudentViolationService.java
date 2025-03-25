package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentViolationDTO;

import java.util.List;

public interface StudentViolationService {
    List<StudentViolationDTO> getAllStudentViolations(String classifyBy, String status,
                                                      int pageNo, int pageSize, String sortBy, String sortDir);
}
