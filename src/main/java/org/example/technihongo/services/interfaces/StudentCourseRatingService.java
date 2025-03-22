package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentCourseRatingDTO;
import org.example.technihongo.dto.StudentCourseRatingRequest;

import java.math.BigDecimal;
import java.util.List;

public interface StudentCourseRatingService {
    StudentCourseRatingDTO createRating(Integer studentId, StudentCourseRatingRequest request);
    StudentCourseRatingDTO getRatingById(Integer ratingId);
    List<StudentCourseRatingDTO> getAllRatings();
    StudentCourseRatingDTO updateRating(Integer ratingId, Integer studentId, StudentCourseRatingRequest request);
    void deleteRating(Integer ratingId);
    BigDecimal getAverageRatingForCourse(Integer courseId);
    List<StudentCourseRatingDTO> getAllRatingsForCourse(Integer courseId);
    List<String> getAllReviewsForCourse(Integer courseId);
    StudentCourseRatingDTO getRatingByStudentAndCourse(Integer studentId, Integer courseId);
}
