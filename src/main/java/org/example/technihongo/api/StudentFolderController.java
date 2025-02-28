package org.example.technihongo.api;

import org.example.technihongo.dto.StudentFolderDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.StudentFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-folder")
public class StudentFolderController {
    @Autowired
    private StudentFolderService studentFolderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createStudentFolder(@RequestBody StudentFolderDTO folderDTO) {
        try {
            StudentFolderDTO createdFolder = studentFolderService.createStudentFolder(folderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student folder created successfully")
                    .data(createdFolder)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{folderId}")
    public ResponseEntity<ApiResponse> updateStudentFolder(
            @PathVariable Integer folderId,
            @RequestBody StudentFolderDTO folderDTO) {
        try {
            StudentFolderDTO updatedFolder = studentFolderService.updateStudentFolder(folderId, folderDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student folder updated successfully")
                    .data(updatedFolder)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/deleteFolder/{folderId}")
    public ResponseEntity<ApiResponse> deleteStudentFolder(@PathVariable Integer folderId) {
        try {
            studentFolderService.deleteStudentFolder(folderId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Student folder deleted successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/getStudentFolder/{studentId}")
    public ResponseEntity<ApiResponse> listAllStudentFolders(@PathVariable Integer studentId) {
        try {
            List<StudentFolderDTO> folders = studentFolderService.listAllStudentFolders(studentId);
            if (folders.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No student folders found")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Student folders retrieved successfully")
                        .data(folders)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
}


