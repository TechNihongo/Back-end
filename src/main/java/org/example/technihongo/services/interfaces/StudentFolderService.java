package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.StudentFolderDTO;

import java.util.List;

public interface StudentFolderService {
    StudentFolderDTO createStudentFolder(StudentFolderDTO folderDTO);
    StudentFolderDTO updateStudentFolder(Integer folderId, StudentFolderDTO folderDTO);

    void deleteStudentFolder(Integer folderId);
    List<StudentFolderDTO> listAllStudentFolders(Integer studentId);
}
