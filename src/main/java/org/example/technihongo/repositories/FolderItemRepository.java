package org.example.technihongo.repositories;

import org.example.technihongo.entities.FolderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderItemRepository extends JpaRepository<FolderItem, Integer> {
    List<FolderItem> findByFolderItemId(Integer folderId);
    List<FolderItem> findByStudentFolderFolderId(Integer folderId);
    boolean existsByStudentFolderFolderId(Integer folderId);
    Optional<FolderItem> findByStudentFolderFolderIdAndStudentFlashcardSetStudentSetId(
            Integer folderId, Integer studentSetId);

    long countByStudentFolderFolderId(Integer folderId);
}
