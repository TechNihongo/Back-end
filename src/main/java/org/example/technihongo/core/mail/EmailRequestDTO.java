package org.example.technihongo.core.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailRequestDTO {
    private String toEmail;
    private String subject;
    private String body;
}
