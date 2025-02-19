package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePathCourseDTO {
    private Integer pathId;
    private Integer courseId;
}
