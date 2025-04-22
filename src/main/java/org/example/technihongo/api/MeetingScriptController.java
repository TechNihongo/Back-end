package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.MeetingDTO;
import org.example.technihongo.dto.MeetingScriptDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.Meeting;
import org.example.technihongo.entities.MeetingScript;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.MeetingScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/script")
@RequiredArgsConstructor
public class MeetingScriptController {
    @Autowired
    private MeetingScriptService meetingScriptService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<ApiResponse> getListScriptsByMeetingId(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Integer meetingId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "scriptOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){
        try{
            PageResponseDTO<MeetingScript> scriptList = meetingScriptService.getListScriptsByMeetingId(meetingId, pageNo, pageSize, sortBy, sortDir);
            if (scriptList.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Danh sách kịch bản trống!")
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Lấy danh sách kịch bản")
                        .data(scriptList)
                        .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Lấy danh sách kịch bản thất bại: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{scriptId}")
    public ResponseEntity<ApiResponse> viewScript(
            @PathVariable Integer scriptId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        try{
            MeetingScript script = meetingScriptService.getScriptById(scriptId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get script")
                    .data(script)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createScript(
            @RequestBody MeetingScriptDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if(roleId == 1 || roleId == 2) {
                    MeetingScript script = meetingScriptService.createScript(dto);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Tạo kịch bản thành công!")
                            .data(script)
                            .build());
                }
                else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.builder()
                                    .success(false)
                                    .message("Không có quyền")
                                    .build());
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Không có quyền")
                                .build());
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Tạo kịch bản thất bại: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{scriptId}")
    public ResponseEntity<ApiResponse> updateScript(
            @PathVariable Integer scriptId,
            @RequestBody MeetingScriptDTO dto,
            @RequestHeader("Authorization") String authorizationHeader) {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if(roleId == 1 || roleId == 2) {
                    meetingScriptService.updateScript(scriptId, dto);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Cập nhật kịch bản thành công")
                            .build());
                }
                else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.builder()
                                    .success(false)
                                    .message("Không có quyền")
                                    .build());
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Không có quyền")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Cập nhật kịch bản thất bại: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{scriptId}")
    public ResponseEntity<ApiResponse> deleteScript(
            @PathVariable Integer scriptId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if(roleId == 1 || roleId == 2) {
                    meetingScriptService.deleteScript(scriptId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Xóa kịch bản thành công")
                            .build());
                }
                else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.builder()
                                    .success(false)
                                    .message("Không có quyền")
                                    .build());
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder()
                                .success(false)
                                .message("Không có quyền")
                                .build());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Xóa kịch bản thất bại: " + e.getMessage())
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
