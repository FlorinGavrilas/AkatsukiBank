package com.ing.tech.bank.service;

import com.ing.tech.bank.model.entities.Currency;
import com.ing.tech.bank.repository.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency getExchangeRates(String baseCurrency) {
        return currencyRepository.findCurrencyByBaseCurrency(baseCurrency);
    }

    public double getConversionRate(String baseCurrency, String receiverCurrency) {
        Currency currency = currencyRepository.findCurrencyByBaseCurrency(baseCurrency);

        switch (receiverCurrency) {
            case "RON":
                return currency.getToRon();
            case "EUR":
                return currency.getToEur();
            case "USD":
                return currency.getToUsd();
            case "GBP":
                return currency.getToGbp();
            default:
                throw new RuntimeException("Currency not found.");
        }
    }
}
