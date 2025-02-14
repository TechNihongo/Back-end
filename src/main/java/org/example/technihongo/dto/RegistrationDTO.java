package org.example.technihongo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.technihongo.entities.Student;
import org.example.technihongo.enums.OccupationStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$",
            message = "Password must contain at least 8 characters, including uppercase, lowercase, a digit, and a special character"
    )
    private String password;

    private String confirmPassword;

    private LocalDate dob;
    private OccupationStatus occupation;
}

