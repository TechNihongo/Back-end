package org.example.technihongo.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "[ResourceFile]")
public class ResourceFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false, referencedColumnName = "resource_id")
    private LearningResource learningResource;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false, referencedColumnName = "type_id")
    private ResourceType resourceType;

    @Column(name = "content_url")
    private String url;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "display_order")
    private String displayOrder;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
