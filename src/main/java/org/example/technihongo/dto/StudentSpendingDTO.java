package org.example.technihongo.dto;

import lombok.Data;

@Data
public class StudentSpendingDTO {
    private Integer studentId;
    private Double totalSpent;

    public StudentSpendingDTO(Integer studentId, Double totalSpent) {
        this.studentId = studentId;
        this.totalSpent = totalSpent != null ? totalSpent : 0.0;
    }
}
