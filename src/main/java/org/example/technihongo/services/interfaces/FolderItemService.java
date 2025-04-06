package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.dto.RemoveItemDTO;

import java.util.List;

public interface FolderItemService {
    FolderItemDTO addFolderItem(Integer studentId, FolderItemDTO folderItemDTO);
    List<FolderItemDTO> addMultipleFolderItems(Integer studentId, List<FolderItemDTO> folderItemDTOs);
    void removeFolderItem(Integer studentId, RemoveItemDTO request);
    void removeMultipleFolderItems(Integer studentId, List<RemoveItemDTO> requests);
    List<FolderItemDTO> getFolderItemsByFolderId(Integer studentId, Integer folderId);
    List<FolderItemDTO> searchItems(Integer studentId, Integer folderId, String searchTerm);
    FolderItemDTO moveItem(Integer studentId, Integer folderItemId, Integer targetFolderId);

    int countFolderItemsInFolder(Integer studentId, Integer folderId);


}
