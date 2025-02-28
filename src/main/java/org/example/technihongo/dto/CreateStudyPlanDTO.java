package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudyPlanDTO {
    private Integer courseId;
    private String title;
    private String description;
    private Integer hoursPerDay;
}
