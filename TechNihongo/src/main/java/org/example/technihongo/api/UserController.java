package org.example.technihongo.api;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.LoginResponseDTO;
import org.example.technihongo.entities.User;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles user login requests.
     *
     * @param email the username of the user
     * @param password the password of the user
     * @return ResponseEntity containing the login result
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestParam String email, @RequestParam String password) {
        try {
            LoginResponseDTO response = userService.login(email, password);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            String errorMessage = "Login failed: " + e.getMessage();
            LoginResponseDTO errorResponse = new LoginResponseDTO(null, null, null, null, false, errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUser() throws Exception {
        List<User> userList = userService.userList();
        if(userList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(false)
                    .message("List user is empty!")
                    .build());
        }else{
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Get All User")
                    .data(userList)
                    .build());
        }
    }
}
