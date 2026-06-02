package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Data;
import org.example.technihongo.enums.ViolationStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportViolationRequestDTO {
    private String classifyBy;
    private Integer contentId;
    private String description;
}
