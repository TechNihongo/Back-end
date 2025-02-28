package org.example.technihongo.api;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.dto.RemoveItemDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.FolderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/folder-item")
public class FolderItemController {
    @Autowired
    private FolderItemService folderItemService;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addFolderItem(@RequestBody FolderItemDTO folderItemDTO) {
        try {
            FolderItemDTO createdFolderItem = folderItemService.addFolderItem(folderItemDTO);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Flashcard set added to folder successfully")
                    .data(createdFolderItem)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeFolderItem(
            @RequestBody RemoveItemDTO request) {
        try {
            folderItemService.removeFolderItem(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Folder item removed successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Unexpected error: " + e.getMessage())
                            .build());
        }
    }
    @GetMapping("/folder/{folderId}")
    public ResponseEntity<ApiResponse> getFolderItems(@PathVariable Integer folderId) {
        try {
            List<FolderItemDTO> folderItems = folderItemService.getFolderItemsByFolderId(folderId);

            if (folderItems.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("This folder is empty!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get All Folder Items")
                        .data(folderItems)
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
