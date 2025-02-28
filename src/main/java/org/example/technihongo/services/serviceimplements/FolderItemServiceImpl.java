package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.dto.RemoveItemDTO;
import org.example.technihongo.entities.FolderItem;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudentFolder;
import org.example.technihongo.repositories.FolderItemRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentFolderRepository;
import org.example.technihongo.services.interfaces.FolderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FolderItemServiceImpl implements FolderItemService {

    @Autowired
    private FolderItemRepository folderItemRepository;

    @Autowired
    private StudentFolderRepository studentFolderRepository;

    @Autowired
    private StudentFlashcardSetRepository studentFlashcardSetRepository;

    @Override
    public FolderItemDTO addFolderItem(FolderItemDTO folderItemDTO) {
        StudentFolder studentFolder = studentFolderRepository.findById(folderItemDTO.getFolderId())
                .orElseThrow(() -> new RuntimeException("Student folder not found with ID: " + folderItemDTO.getFolderId()));

        if (studentFolder.isDeleted()) {
            throw new IllegalStateException("Cannot add item to a deleted folder");
        }

        StudentFlashcardSet studentFlashcardSet = studentFlashcardSetRepository.findById(folderItemDTO.getStudentSetId())
                .orElseThrow(() -> new RuntimeException("Student flashcard set not found with ID: " + folderItemDTO.getStudentSetId()));

        Optional<FolderItem> existingItem = folderItemRepository.findByStudentFolderFolderIdAndStudentFlashcardSetStudentSetId(
                folderItemDTO.getFolderId(),
                folderItemDTO.getStudentSetId());

        if (existingItem.isPresent()) {
            throw new IllegalStateException("This flashcard set is already in the folder");
        }

        FolderItem folderItem = FolderItem.builder()
                .studentFolder(studentFolder)
                .studentFlashcardSet(studentFlashcardSet)
                .build();

        FolderItem savedFolderItem = folderItemRepository.save(folderItem);

        return convertToDTO(savedFolderItem);
    }

    @Override
    public void removeFolderItem(RemoveItemDTO removeItemDTO) {
        Integer folderItemId = removeItemDTO.getFolderItemId();
        Integer studentId = removeItemDTO.getStudentId();

        FolderItem folderItem = folderItemRepository.findById(folderItemId)
                .orElseThrow(() -> new RuntimeException("Folder item not found with ID: " + folderItemId));

        StudentFlashcardSet flashcardSet = folderItem.getStudentFlashcardSet();
        Integer flashcardStudentId = flashcardSet.getStudentSetId();
        if (!flashcardStudentId.equals(studentId)) {
            throw new IllegalStateException("You do not have permission to remove this flashcard set from the folder");
        }

        folderItemRepository.deleteById(folderItemId);
    }

    @Override
    public List<FolderItemDTO> getFolderItemsByFolderId(Integer folderId) {
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID cannot be null");
        }

        StudentFolder folder = studentFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Student folder not found with ID: " + folderId));

        if (folder.isDeleted()) {
            throw new IllegalStateException("Cannot get items from a deleted folder");
        }

        List<FolderItem> folderItems = folderItemRepository.findByStudentFolderFolderId(folderId);

        return folderItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private FolderItemDTO convertToDTO(FolderItem item) {
        return new FolderItemDTO(
                item.getFolderItemId(),
                item.getStudentFolder().getFolderId(),
                item.getStudentFlashcardSet().getStudentSetId()
        );
    }
}

