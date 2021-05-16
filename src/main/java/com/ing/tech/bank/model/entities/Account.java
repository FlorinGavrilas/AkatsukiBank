package com.ing.tech.bank.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "account")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private String currency;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "account_transaction",
            joinColumns =  @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id")
    )
    private Set<Transaction> transactions;

    public Account(String username, String iban, double balance, String currency) {
        this.username = username;
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
    }
}
