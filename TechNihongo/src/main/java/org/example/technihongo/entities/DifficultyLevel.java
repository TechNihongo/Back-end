package org.example.technihongo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DifficultyLevel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifficultyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer levelId;

    @Column(name ="code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "order_sequence")
    private Integer orderSequence;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
