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
@Table(name = "[Achievement]")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Integer achievementId;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "badgeName")
    private String badgeName;

    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "category")
    private String category;

    @Column(name = "conditionType")
    private String conditionType;

    @Column(name = "conditionValue")
    private Integer conditionValue;
    
    @Column(name = "isActive")
    @Builder.Default
    private boolean isActive = true;


    @Column(name = "displayOrder")
    private Integer displayOrder;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
