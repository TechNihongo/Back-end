package org.example.technihongo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.technihongo.enums.OccupationStatus;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "bio")
    private String bio;

    @Column(name = "daily_goal")
    private Integer dailyGoal = 60;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation_status")
    private OccupationStatus occupation;

    @Column(name = "reminder_enabled")
    private boolean reminderEnabled = false;

    @Column(name = "reminder_time")
    private LocalTime reminderTime;

    @Column(name = "violation_count")
    private Integer violationCount = 0;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "level_id")
    private DifficultyLevel difficultyLevel;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}