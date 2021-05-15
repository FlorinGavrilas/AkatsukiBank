package com.ing.tech.bank.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ibanSender;

    @Column(nullable = false)
    private String ibanReceiver;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String message;

    public Transaction(String ibanSender, String ibanReceiver, String type, double amount, LocalDate date, String message) {
        this.ibanSender = ibanSender;
        this.ibanReceiver = ibanReceiver;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.message = message;
    }
}
