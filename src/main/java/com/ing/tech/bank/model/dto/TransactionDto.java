package com.ing.tech.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String ibanSender;
    private String ibanReceiver;
    private String type;
    private double amount;
    private LocalDate date;
    private String message;
}
