package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwitchStudyPlanRequestDTO {
    private Integer studentId;
    private Integer newStudyPlanId;
    private Integer currentStudyPlanId;
}
