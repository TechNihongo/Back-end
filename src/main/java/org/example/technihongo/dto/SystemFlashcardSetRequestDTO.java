package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemFlashcardSetRequestDTO {
    private String title;
    private String description;
    private boolean isPublic;
}
