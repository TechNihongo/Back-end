package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyPlanDTO {
    private Integer studyPlanId;
    private Integer courseId;
    private String title;
    private String description;
    private Integer hoursPerDay;
    private boolean isDefault;
    private boolean isActive;

}
