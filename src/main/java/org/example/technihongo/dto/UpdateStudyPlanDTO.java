package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudyPlanDTO {
    private String title;
    private String description;
    private Integer hours_per_day;
    private Integer total_months;
    private Boolean isActive;
}
