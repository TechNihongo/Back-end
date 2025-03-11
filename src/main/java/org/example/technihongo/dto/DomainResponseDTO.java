package org.example.technihongo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainResponseDTO {
    private Integer domainId;
    private String tag;
    private String name;
    private String description;
    private Integer parentDomainId;
}
