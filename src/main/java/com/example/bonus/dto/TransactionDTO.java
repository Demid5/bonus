package com.example.bonus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private String status;
}
