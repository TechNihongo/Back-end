package org.example.technihongo.services.serviceimplements;

import org.example.technihongo.dto.StudentFolderDTO;
import org.example.technihongo.entities.Student;
import org.example.technihongo.entities.StudentFolder;
import org.example.technihongo.repositories.FolderItemRepository;
import org.example.technihongo.repositories.StudentFolderRepository;
import org.example.technihongo.repositories.StudentRepository;
import org.example.technihongo.services.interfaces.StudentFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentFolderServiceImpl implements StudentFolderService {

    @Autowired
    private StudentFolderRepository studentFolderRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FolderItemRepository folderItemRepository;

    @Override
    public StudentFolderDTO createStudentFolder(StudentFolderDTO folderDTO) {

        if (folderDTO.getName() == null || folderDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Folder name is required");
        }

        if (folderDTO.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }

        Student student = studentRepository.findById(folderDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + folderDTO.getStudentId()));

        StudentFolder folder = StudentFolder.builder()
                .student(student)
                .name(folderDTO.getName())
                .description(folderDTO.getDescription())
                .build();

        folder = studentFolderRepository.save(folder);

        return convertToDTO(folder);
    }

    @Override
    public StudentFolderDTO updateStudentFolder(Integer folderId, StudentFolderDTO folderDTO) {
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID cannot be null");
        }

        StudentFolder folder = studentFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        if (folderDTO.getName() != null && !folderDTO.getName().trim().isEmpty()) {
            folder.setName(folderDTO.getName());
        }

        if (folderDTO.getDescription() != null) {
            folder.setDescription(folderDTO.getDescription());
        }

        if (folderDTO.getStudentId() != null &&
                (folder.getStudent() == null || !folderDTO.getStudentId().equals(folder.getStudent().getStudentId()))) {
            Student student = studentRepository.findById(folderDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + folderDTO.getStudentId()));
            folder.setStudent(student);
        }

        folder = studentFolderRepository.save(folder);
        return convertToDTO(folder);
    }

    @Override
    public void deleteStudentFolder(Integer folderId) {
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID cannot be null");
        }

        StudentFolder folder = studentFolderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found with ID: " + folderId));

        if (folderItemRepository.existsByStudentFolderFolderId(folderId)) {
            throw new IllegalStateException("Cannot delete folder with ID: " + folderId + " because it contains items");
        }

        folder.setDeleted(true);
        studentFolderRepository.save(folder);
    }

    @Override
    public List<StudentFolderDTO> listAllStudentFolders(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        // Kiểm tra xem student có tồn tại không
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        List<StudentFolder> folders = studentFolderRepository.findByStudentStudentId(studentId);

        return folders.stream()
                .filter(folder -> !folder.isDeleted())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentFolderDTO convertToDTO(StudentFolder folder) {
        StudentFolderDTO dto = new StudentFolderDTO();
        dto.setFolderId(folder.getFolderId());
        dto.setStudentId(folder.getStudent().getStudentId());
        dto.setName(folder.getName());
        dto.setDescription(folder.getDescription());
        return dto;
    }
}