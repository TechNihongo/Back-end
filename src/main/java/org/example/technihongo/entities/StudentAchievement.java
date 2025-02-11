package org.example.technihongo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentAchievement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_achievement_id")
    private Integer studentAchievementId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false, referencedColumnName = "achievement_id")
    private Achievement achievement;

    @CreationTimestamp
    @Column(name = "achieved_at", updatable = false, nullable = false)
    private LocalDateTime achievedAt;
}
