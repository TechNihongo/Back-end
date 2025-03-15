package org.example.technihongo.services.serviceimplements;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.entities.FolderItem;
import org.example.technihongo.entities.StudentFlashcardSet;
import org.example.technihongo.entities.StudentFolder;
import org.example.technihongo.repositories.FolderItemRepository;
import org.example.technihongo.repositories.StudentFlashcardSetRepository;
import org.example.technihongo.repositories.StudentFolderRepository;

//public class FolderItemServiceImplTest {
//
//    @Mock
//    private FolderItemRepository folderItemRepository;
//
//    @Mock
//    private StudentFolderRepository studentFolderRepository;
//
//    @Mock
//    private StudentFlashcardSetRepository studentFlashcardSetRepository;
//
//    @InjectMocks
//    private FolderItemServiceImpl folderItemService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // Successfully creates and saves a new folder item when valid folder and flashcard set IDs are provided
//    @Test
//    public void test_add_folder_item_success() {
//        // Arrange
//        StudentFolder mockFolder = new StudentFolder();
//        mockFolder.setFolderId(1);
//
//        StudentFlashcardSet mockSet = new StudentFlashcardSet();
//        mockSet.setStudentSetId(2);
//
//        FolderItem mockSavedItem = FolderItem.builder()
//            .folderItemId(1)
//            .studentFolder(mockFolder)
//            .studentFlashcardSet(mockSet)
//            .build();
//
//        when(studentFolderRepository.findById(1)).thenReturn(Optional.of(mockFolder));
//        when(studentFlashcardSetRepository.findById(2)).thenReturn(Optional.of(mockSet));
//        when(folderItemRepository.save(any(FolderItem.class))).thenReturn(mockSavedItem);
//
//        FolderItemDTO dto = new FolderItemDTO(null, 1, 2);
//
//        // Act
//        FolderItemDTO result = folderItemService.addFolderItem(dto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(Integer.valueOf(1), result.getFolderItemId());
//        assertEquals(Integer.valueOf(1), result.getFolderId());
//        assertEquals(Integer.valueOf(2), result.getStudentSetId());
//
//        verify(folderItemRepository).save(any(FolderItem.class));
//    }
//
//    // Throws RuntimeException when folder ID does not exist
//    @Test
//    public void test_add_folder_item_invalid_folder_id() {
//        // Arrange
//        when(studentFolderRepository.findById(999)).thenReturn(Optional.empty());
//
//        FolderItemDTO dto = new FolderItemDTO(null, 999, 1);
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            folderItemService.addFolderItem(dto);
//        });
//
//        assertEquals("Student folder not found with ID: 999", exception.getMessage());
//        verify(studentFolderRepository).findById(999);
//        verify(studentFlashcardSetRepository, never()).findById(any());
//    }
//
//    // Throws RuntimeException when flashcard set ID does not exist
//    @Test
//    public void test_add_folder_item_invalid_flashcard_set_id() {
//        // Arrange
//        StudentFolder mockFolder = new StudentFolder();
//        mockFolder.setFolderId(1);
//
//        when(studentFolderRepository.findById(1)).thenReturn(Optional.of(mockFolder));
//        when(studentFlashcardSetRepository.findById(999)).thenReturn(Optional.empty());
//
//        FolderItemDTO dto = new FolderItemDTO(null, 1, 999);
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            folderItemService.addFolderItem(dto);
//        });
//
//        assertEquals("Student flashcard set not found with ID: 999", exception.getMessage());
//        verify(studentFolderRepository).findById(1);
//        verify(studentFlashcardSetRepository).findById(999);
//        verify(folderItemRepository, never()).save(any());
//    }
//}
