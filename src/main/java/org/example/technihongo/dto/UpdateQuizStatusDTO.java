package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuizStatusDTO {
    private Boolean isPublic;
    private Boolean isDeleted;
}
