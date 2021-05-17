package com.ing.tech.bank.repository;

import com.ing.tech.bank.model.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findCurrencyByBaseCurrency(String currency);
}
