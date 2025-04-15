package org.example.technihongo.dto;

import lombok.*;

import org.example.technihongo.entities.StudentViolation;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ViolationSummaryProjection {
    private Integer entityId;
    private long totalViolations;

    public ViolationSummaryProjection(Integer entityId, long totalViolations) {
        this.entityId = entityId;
        this.totalViolations = totalViolations;
    }
}