package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollStudyPlanRequest {
    private Integer studentId;
    private Integer studyPlanId;
}
