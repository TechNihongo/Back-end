package org.example.technihongo.api;

import org.example.technihongo.dto.FolderItemDTO;
import org.example.technihongo.dto.RemoveItemDTO;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.FolderItemService;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.services.interfaces.StudentService;
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
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addFolderItem(
            @RequestBody FolderItemDTO folderItemDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);


                FolderItemDTO createdFolderItem = folderItemService.addFolderItem(studentId, folderItemDTO);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Flashcard set added to folder successfully")
                        .data(createdFolderItem)
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
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
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeFolderItem(
            @RequestBody RemoveItemDTO request,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                folderItemService.removeFolderItem(studentId, request);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Folder item removed successfully")
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
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
    public ResponseEntity<ApiResponse> getFolderItems(
            @PathVariable Integer folderId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                List<FolderItemDTO> folderItems = folderItemService.getFolderItemsByFolderId(studentId, folderId);

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
            } else {
                throw new Exception("Authorization failed!");
            }
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
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PostMapping("/add-multiple")
    public ResponseEntity<ApiResponse> addMultipleFolderItems(
            @RequestBody List<FolderItemDTO> folderItemDTOs,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                List<FolderItemDTO> createdFolderItems = folderItemService.addMultipleFolderItems(studentId, folderItemDTOs);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Multiple flashcard sets added to folder successfully")
                        .data(createdFolderItems)
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
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
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/remove-multiple")
    public ResponseEntity<ApiResponse> removeMultipleFolderItems(
            @RequestBody List<RemoveItemDTO> requests,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                folderItemService.removeMultipleFolderItems(studentId, requests);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Multiple folder items removed successfully")
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
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

    @GetMapping("/search/{folderId}")
    public ResponseEntity<ApiResponse> searchFolderItems(
            @PathVariable Integer folderId,
            @RequestParam String searchTerm,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                List<FolderItemDTO> foundItems = folderItemService.searchItems(studentId, folderId, searchTerm);

                if (foundItems.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(false)
                            .message("No items found matching the search term")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Found folder items")
                            .data(foundItems)
                            .build());
                }
            } else {
                throw new Exception("Authorization failed!");
            }
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
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PutMapping("/move/{folderItemId}/{targetFolderId}")
    public ResponseEntity<ApiResponse> moveFolderItem(
            @PathVariable Integer folderItemId,
            @PathVariable Integer targetFolderId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                Integer userId = jwtUtil.extractUserId(token);
                Integer studentId = studentService.getStudentIdByUserId(userId);

                FolderItemDTO movedItem = folderItemService.moveItem(studentId, folderItemId, targetFolderId);

                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Folder item moved successfully")
                        .data(movedItem)
                        .build());
            } else {
                throw new Exception("Authorization failed!");
            }
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
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }
}