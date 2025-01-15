package org.example.technihongo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[StudentAchievementProgress]")
public class StudentAchievementProgress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "progress_id")
  private Integer progressId;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne
  @JoinColumn(name = "achievement_id")
  private Achievement achievement;

  @Column(name = "current_value")
  private Integer currentValue;

  @Column(name = "required_value")
  private Integer requiredValue;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

  @Column(name = "created_at")
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
