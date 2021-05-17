package com.ing.tech.bank.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    private String baseCurrency;
    private double toRon;
    private double toEur;
    private double toUsd;
    private double toGbp;
}
