package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.MeetingDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.entities.Meeting;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllMeetings(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "meetingId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                    PageResponseDTO<Meeting> meetingList = meetingService.getAllMeetings(pageNo, pageSize, sortBy, sortDir);
                    if (meetingList.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Danh sách buổi họp trống!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Lấy danh sách buổi họp")
                                .data(meetingList)
                                .build());
                    }
                } else {
                    PageResponseDTO<Meeting> meetingList = meetingService.getAllActiveMeetings(pageNo, pageSize, sortBy, sortDir);
                    if (meetingList.getContent().isEmpty()) {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Danh sách buổi họp trống!")
                                .build());
                    } else {
                        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .message("Lấy danh sách buổi họp hoạt động")
                                .data(meetingList)
                                .build());
                    }
                }
            }
            else {
                PageResponseDTO<Meeting> meetingList = meetingService.getAllActiveMeetings(pageNo, pageSize, sortBy, sortDir);
                if (meetingList.getContent().isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Danh sách buổi họp trống!")
                            .build());
                } else {
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Lấy danh sách buổi họp hoạt động")
                            .data(meetingList)
                            .build());
                }
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
                            .message("Lấy danh sách buổi họp thất bại: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<ApiResponse> viewMeeting(
            @PathVariable Integer meetingId,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if (roleId == 1 || roleId == 2) {
                   Meeting meeting = meetingService.getMeetingById(meetingId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Meeting")
                            .data(meeting)
                            .build());
                }
                else{
                    Meeting meeting = meetingService.getActiveMeetingById(meetingId);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Get Meeting")
                            .data(meeting)
                            .build());
                }
            }
            else {
                Meeting meeting = meetingService.getActiveMeetingById(meetingId);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Get Meeting")
                        .data(meeting)
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
    @PreAuthorize("hasRole('ROLE_Content Manager')")
    public ResponseEntity<ApiResponse> createMeeting(
            @RequestBody MeetingDTO meetingDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if(roleId == 1 || roleId == 2) {
                    Integer userId = jwtUtil.extractUserId(token);
                    Meeting meeting = meetingService.createMeeting(userId, meetingDTO);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Tạo buổi họp thành công!")
                            .data(meeting)
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
                            .message("Tạo buổi họp thất bại: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Internal Server Error: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{meetingId}")
    @PreAuthorize("hasRole('ROLE_Content Manager')")
    public ResponseEntity<ApiResponse> updateMeeting(
            @PathVariable Integer meetingId,
            @RequestBody MeetingDTO meetingDTO,
            @RequestHeader("Authorization") String authorizationHeader) {
        try{
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                int roleId = jwtUtil.extractUserRoleId(token);

                if(roleId == 1 || roleId == 2) {
                    meetingService.updateMeeting(meetingId, meetingDTO);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .message("Cập nhật buổi họp thành công")
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
                            .message("Cập nhật buổi họp thất bại: " + e.getMessage())
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
