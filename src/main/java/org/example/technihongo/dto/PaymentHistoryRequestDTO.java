package org.example.technihongo.dto;

import lombok.Data;
import org.example.technihongo.enums.TransactionStatus;

@Data
public class PaymentHistoryRequestDTO {
    private Integer studentId;
    private TransactionStatus transactionStatus;
}