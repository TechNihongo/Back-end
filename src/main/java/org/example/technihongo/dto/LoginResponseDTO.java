package org.example.technihongo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Integer userId;
    private String userName;
    private String email;
    private String profileImg;
    private String role;
    private boolean success;
    private String message;


}
