package org.example.technihongo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentStudyPlanDTO {
    private Integer studentPlanId;
    private Integer studentId;
    private Integer studyPlanId;
    private Integer previousPlanId;
    private LocalDateTime startDate;
    private String status;
    private LocalDateTime switchDate;

}
