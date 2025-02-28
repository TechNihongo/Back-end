package org.example.technihongo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "FolderItem")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_item_id")
    private Integer folderItemId;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "folder_id", nullable = false, referencedColumnName = "folder_id")
    private StudentFolder studentFolder;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "student_set_id", nullable = false, referencedColumnName = "student_set_id")
    private StudentFlashcardSet studentFlashcardSet;


    @CreationTimestamp
    @Column(name = "added_at")
    private LocalDateTime addedAt;
}