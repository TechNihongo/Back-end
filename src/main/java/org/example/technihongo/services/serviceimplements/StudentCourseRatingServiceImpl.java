package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.StudentCourseRatingDTO;
import org.example.technihongo.dto.StudentCourseRatingRequest;
import org.example.technihongo.entities.Course;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentCourseRating;
import org.example.technihongo.entities.User;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.exception.UnauthorizedAccessException;
import org.example.technihongo.repositories.*;
import org.example.technihongo.services.interfaces.StudentCourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentCourseRatingServiceImpl implements StudentCourseRatingService {
    @Autowired
    private StudentCourseRatingRepository studentCourseRatingRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseProgressRepository studentCourseProgressRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public StudentCourseRatingDTO createRating(Integer studentId, StudentCourseRatingRequest request) {
        return createRatingWithStudentId(studentId, request);
    }

    @Override
    public StudentCourseRatingDTO getRatingById(Integer ratingId) {
        StudentCourseRating rating = studentCourseRatingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        return mapToDTO(rating);
    }

    @Override
    public List<StudentCourseRatingDTO> getAllRatings() {
        List<StudentCourseRating> ratings = studentCourseRatingRepository.findAll();
        return ratings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentCourseRatingDTO updateRating(Integer ratingId, Integer studentId, StudentCourseRatingRequest request) {
        return updateRatingWithStudentId(ratingId, studentId, request);
    }

    @Override
    public void deleteRating(Integer ratingId) {
        StudentCourseRating rating = studentCourseRatingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));
        studentCourseRatingRepository.delete(rating);
    }

    @Override
    public BigDecimal getAverageRatingForCourse(Integer courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
        List<StudentCourseRating> ratings = studentCourseRatingRepository.findByCourseCourseId(courseId);
        if (ratings.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal averageRating = BigDecimal.valueOf(ratings.stream()
                        .mapToInt(StudentCourseRating::getRating)
                        .average()
                        .orElse(0.0))
                .setScale(2, RoundingMode.HALF_UP);
        return averageRating;
    }

    @Override
    public List<StudentCourseRatingDTO> getAllRatingsForCourse(Integer courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }

        List<StudentCourseRating> ratings = studentCourseRatingRepository.findByCourseCourseId(courseId);
        return ratings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllReviewsForCourse(Integer courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
        List<StudentCourseRating> ratings = studentCourseRatingRepository.findByCourseCourseId(courseId);
        return ratings.stream()
                .map(StudentCourseRating::getReview)
                .filter(review -> review != null && !review.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public StudentCourseRatingDTO getRatingByStudentAndCourse(Integer studentId, Integer courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
        StudentCourseRating rating = studentCourseRatingRepository.findByStudentStudentIdAndCourseCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalStateException("No rating found for student ID: " + studentId + " and course ID: " + courseId));
        return mapToDTO(rating);
    }

    private StudentCourseRatingDTO createRatingWithStudentId(Integer studentId, StudentCourseRatingRequest request) {
        validateRequest(request);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        Course course = courseRepository.findByCourseId(request.getCourseId());
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + request.getCourseId());
        }

        boolean isEnrolled = studentCourseProgressRepository.existsByStudentStudentIdAndCourseCourseId(studentId, request.getCourseId());
        if (!isEnrolled) {
            throw new UnauthorizedAccessException("Student must enroll in the course before rating.");
        }

        if (studentCourseRatingRepository.existsByStudentStudentIdAndCourseCourseId(studentId, request.getCourseId())) {
            throw new IllegalStateException("Student has already rated this course.");
        }

        StudentCourseRating rating = StudentCourseRating.builder()
                .student(student)
                .course(course)
                .rating(request.getRating())
                .review(request.getReview())
                .build();

        StudentCourseRating savedRating = studentCourseRatingRepository.save(rating);
        return mapToDTO(savedRating);
    }

    private StudentCourseRatingDTO updateRatingWithStudentId(Integer ratingId, Integer studentId, StudentCourseRatingRequest request) {
        validateRequest(request);

        StudentCourseRating rating = studentCourseRatingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with ID: " + ratingId));

        if (!rating.getStudent().getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You are not authorized to update this rating.");
        }

        Course course = courseRepository.findByCourseId(request.getCourseId());
        if (course == null) {
            throw new ResourceNotFoundException("Course not found with ID: " + request.getCourseId());
        }

        boolean isEnrolled = studentCourseProgressRepository.existsByStudentStudentIdAndCourseCourseId(studentId, request.getCourseId());
        if (!isEnrolled) {
            throw new UnauthorizedAccessException("Student must enroll in the course before updating the rating.");
        }
        rating.setCourse(course);

        if(request.getRating() != null) {
            rating.setRating(request.getRating());
        }

        if(request.getReview() != null) {
            rating.setReview(request.getReview());
        }
        StudentCourseRating updatedRating = studentCourseRatingRepository.save(rating);
        return mapToDTO(updatedRating);
    }

    private void validateRequest(StudentCourseRatingRequest request) {

        if (request.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID is required.");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (request.getReview() != null && request.getReview().trim().isEmpty()) {
            throw new IllegalArgumentException("Review cannot be empty if provided.");
        }
    }

    private StudentCourseRatingDTO mapToDTO(StudentCourseRating rating) {
        User user = userRepository.findByStudentStudentId(rating.getStudent().getStudentId());
        if(user == null){
            throw new ResourceNotFoundException("User not found!");
        }

        StudentCourseRatingDTO dto = new StudentCourseRatingDTO();
        dto.setRatingId(rating.getRatingId());
        dto.setStudentId(rating.getStudent().getStudentId());
        dto.setUserName(user.getUserName());
        dto.setProfileImg(user.getProfileImg());
        dto.setCourseId(rating.getCourse().getCourseId());
        dto.setRating(rating.getRating());
        dto.setReview(rating.getReview());

        return dto;
    }
}
