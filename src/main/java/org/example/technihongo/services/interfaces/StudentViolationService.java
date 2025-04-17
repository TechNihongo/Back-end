package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.*;
import org.example.technihongo.entities.StudentViolation;

public interface StudentViolationService {
    PageResponseDTO<StudentViolation> getAllStudentViolations(String classifyBy, String status,
                                                              int pageNo, int pageSize, String sortBy, String sortDir);
    ReportViolationResponseDTO reportViolation(Integer reportedBy, ReportViolationRequestDTO request);
    HandleViolationResponseDTO handleViolation(Integer violationId, Integer handledBy, HandleViolationRequestDTO request);
    ViolationSummaryDTO getViolationSummary(
            String classifyBy, String statusFilter, Integer entityId, int pageNo, int pageSize, String sortBy, String sortDir);
}
