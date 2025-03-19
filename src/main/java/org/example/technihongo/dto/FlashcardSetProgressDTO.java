package org.example.technihongo.dto;

import lombok.*;
import org.example.technihongo.enums.CompletionStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlashcardSetProgressDTO {
    private Integer setId;
    private String title;
    private Integer totalCards;
    private Integer cardStudied;
    private String completionStatus;
    private LocalDateTime lastStudied;
    private Integer studyCount;
    private CompletionStatus completableStatus;
    private String setType;

}
