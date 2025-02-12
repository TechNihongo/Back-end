package org.example.technihongo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUserInfoDTO {
    private String email;
    private String name;
    private String picture;
    private String sub; // Google's user ID
    private boolean email_verified;
}
