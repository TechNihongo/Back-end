package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.dto.RemoveItemDTO;

import java.util.List;

public interface FolderItemService {
    FolderItemDTO addFolderItem(FolderItemDTO folderItemDTO);
    void removeFolderItem(RemoveItemDTO removeItemDTO);
    List<FolderItemDTO> getFolderItemsByFolderId(Integer folderId);
}
