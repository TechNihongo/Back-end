package org.example.technihongo.dto;

import lombok.Builder;
import lombok.Data;
import org.example.technihongo.enums.ViolationStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentViolationDTO {
    private Integer violationId;
    private Integer studentSetId;
    private Integer ratingId;
    private String description;
    private String actionTaken;
    private Integer reportedById;
    private Integer handledById;
    private ViolationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
