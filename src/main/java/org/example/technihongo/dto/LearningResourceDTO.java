package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningResourceDTO {
    private String title;
    private String description;
    private Integer domainId;
    private String videoUrl;
    private String videoFilename;
    private String pdfUrl;
    private String pdfFilename;
    private Boolean isPremium;
}
