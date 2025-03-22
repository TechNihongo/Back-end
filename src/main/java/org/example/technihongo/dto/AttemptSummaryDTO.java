package org.example.technihongo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttemptSummaryDTO {
    private Integer attemptNumber;
    private BigDecimal score;
    private boolean isPassed;
    private LocalDateTime dateTaken;
    private Boolean isCompleted;
}
