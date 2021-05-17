package com.ing.tech.bank.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_currency", nullable = false)
    private String baseCurrency;

    @Column(name = "to_ron", nullable = false)
    private double toRon;

    @Column(name = "to_eur", nullable = false)
    private double toEur;

    @Column(name = "to_usd", nullable = false)
    private double toUsd;

    @Column(name = "to_gbp", nullable = false)
    private double toGbp;
}
