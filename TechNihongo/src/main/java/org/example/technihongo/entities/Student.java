package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[Student]")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bio")
    private String bio;

    @Column(name = "daily_goal_minutes")
    @Builder.Default
    private Integer dailyGoal = 60;

    @Column(name = "reminder_enabled")
    private boolean reminderEnabled;

    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
