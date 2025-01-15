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
@Table(name = "[StudentFolder]")
public class StudentFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " folder_id")
    private Integer folderId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
}
