package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoginTokenDTO {
    private Integer userId;
    private String token;
    private String tokenType;
    private LocalDateTime expiresAt;
}
