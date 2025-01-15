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
@Table(name = "[FolderItem]")
public class FolderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_item_id")
    private Integer folderItemId;
    
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private StudentFolder folder;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private StudentFlashCardSet studentFlashCardSet;

    @Column(name = "added_at")
    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}
