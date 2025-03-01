package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.ProfileDTO;
import org.example.technihongo.dto.UpdateProfileDTO;
import org.example.technihongo.entities.DifficultyLevel;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.User;
import org.example.technihongo.enums.DifficultyLevelEnum;
import org.example.technihongo.exception.InvalidDifficultyLevelException;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.repositories.DifficultyLevelRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DifficultyLevelRepository difficultyLevelRepository;

    
    @Override
    @Transactional
    public UpdateProfileDTO setDailyGoal(Integer studentId, Integer dailyGoal) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        if (dailyGoal == null) {
            throw new IllegalArgumentException("Daily goal cannot be null");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (dailyGoal < 30) {
            throw new IllegalArgumentException("Fighting, you can do it better!!!");
        }

        student.setDailyGoal(dailyGoal);
        Student savedStudent = studentRepository.save(student);

        return convertToDTO(savedStudent);
    }

    @Override
    @Transactional
    public UpdateProfileDTO updateDifficultyLevel(Integer studentId, DifficultyLevelEnum difficultyLevelEnum) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (difficultyLevelEnum == null) {
            throw new InvalidDifficultyLevelException("Difficulty level cannot be null");
        }
        if (!isValidDifficultyLevel(difficultyLevelEnum)) {
            throw new InvalidDifficultyLevelException("Invalid difficulty level!! The difficulty level must be : N5, N4, N3, N2, N1");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        DifficultyLevel difficultyLevel = difficultyLevelRepository.findByTag(difficultyLevelEnum);
        if (difficultyLevel == null) {
            throw new InvalidDifficultyLevelException("Difficulty level not found in the system: " + difficultyLevelEnum);
        }

        student.setDifficultyLevel(difficultyLevel);
        Student savedStudent = studentRepository.save(student);

        return convertToDTO(savedStudent);
    }

    private boolean isValidDifficultyLevel(DifficultyLevelEnum difficultyLevelEnum) {
        for (DifficultyLevelEnum level : DifficultyLevelEnum.values()) {
            if (level == difficultyLevelEnum) {
                return true;
            }
        }
        return false;
    }

    private UpdateProfileDTO convertToDTO(Student student) {
        if (student == null) {
            return null;
        }

        DifficultyLevelEnum difficultyLevel = null;
        if (student.getDifficultyLevel() != null) {
            difficultyLevel = student.getDifficultyLevel().getTag();
        }

        return UpdateProfileDTO.builder()
                .studentId(student.getStudentId())
                .dailyGoal(student.getDailyGoal())
                .difficultyLevel(difficultyLevel)
                .build();
    }
    @Override
    @Transactional
    public void updateStudentProfile(Integer userId, UpdateProfileDTO dto) {
        Student student = studentRepository.findByUser_UserId(userId);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with id: " + userId);
        }

        if (dto.getProfileImg() != null) {
            User user = student.getUser();
            user.setProfileImg(dto.getProfileImg());
            userRepository.save(user);
        }

        if (dto.getBio() != null) {
            student.setBio(dto.getBio());
        }

        if (dto.getOccupation() != null) {
            student.setOccupation(dto.getOccupation());
        }

        student.setReminderEnabled(dto.isReminderEnabled());
        if (dto.getReminderTime() != null) {
            student.setReminderTime(dto.getReminderTime());
        }

        studentRepository.save(student);
    }

    @Override
    public ProfileDTO getStudentProfile(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        User user = student.getUser();
        if (user == null) {
            throw new ResourceNotFoundException("User not found for student with id: " + studentId);
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserName(user.getUserName());
        profileDTO.setProfileImg(user.getProfileImg());
        profileDTO.setBio(student.getBio());
        profileDTO.setDob(student.getUser().getDob());
        profileDTO.setDailyGoal(student.getDailyGoal());

        if (student.getDifficultyLevel() != null) {
            profileDTO.setDifficultyLevel(student.getDifficultyLevel().getTag());
        }

        profileDTO.setOccupation(student.getOccupation());
        profileDTO.setReminderTime(student.getReminderTime());

        return profileDTO;
    }


}