package org.example.technihongo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseRatingRequest {
    private Integer courseId;
    private Integer rating;
    private String review;
}
