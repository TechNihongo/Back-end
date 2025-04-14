package org.example.technihongo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttemptStatusDTO {
    private Integer consecutiveAttempts;
    private long remainingWaitTime;
    private Integer remainingAttempts;
}
