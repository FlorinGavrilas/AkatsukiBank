package com.ing.tech.bank.service;

import com.ing.tech.bank.exceptions.InsufficientAmountException;
import com.ing.tech.bank.logging.InfoLogger;
import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.model.entities.Transaction;
import com.ing.tech.bank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final InfoLogger infoLogger;

    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public TransactionDto save(TransactionDto transaction) {
        Transaction transactionEntity = new Transaction(transaction.getIbanSender(), transaction.getIbanReceiver(), transaction.getType(), transaction.getAmount(), LocalDate.now(), transaction.getMessage());
        Transaction savedTransaction = transactionRepository.save(transactionEntity);

        return new TransactionDto(savedTransaction.getIbanSender(), savedTransaction.getIbanReceiver(), savedTransaction.getType(), savedTransaction.getAmount(), savedTransaction.getDate(), savedTransaction.getMessage());
    }

    public List<TransactionDto> getAll() {
        return transactionRepository
                .findAll().stream()
                .map(transaction -> new TransactionDto(
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionsByIban(String iban) {
        return transactionRepository.findTransactionByIban(iban)
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getIncomeByIban(String iban) {
        return transactionRepository.findTransactionByIbanReceiverAndType(iban, "1")
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getOutcomeByIban(String iban) {
        return transactionRepository.findTransactionByIbanSenderAndType(iban, "1")
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public boolean validate(TransactionDto transactionDto) {
        AccountDto sender = accountService.getByIban(transactionDto.getIbanSender());

        if (Double.compare(sender.getBalance(),transactionDto.getAmount()) < 1) {
            throw new InsufficientAmountException("Not enough money in balance.");
        }
        return true;
    }

    @Transactional
    public void transfer(TransactionDto transactionDto) {
        if (!validate(transactionDto)) return;

        AccountDto sender = accountService.getByIban(transactionDto.getIbanSender());
        AccountDto receiver = accountService.getByIban(transactionDto.getIbanReceiver());

        double receivedAmount = transactionDto.getAmount() * currencyService.getConversionRate(sender.getCurrency(), receiver.getCurrency());

        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        receiver.setBalance(receiver.getBalance() + receivedAmount);

        accountService.save(sender, sender.getUsername());
        accountService.save(receiver, receiver.getUsername());
    }

    public TransactionDto makeTransaction(TransactionDto transactionDto, String type) {
        transactionDto.setType(type);

        switch (transactionDto.getType()) {
            case "1": // Authorised
            case "3": // Exchange
                transfer(transactionDto);
                break;
            case "2": // Request
                //make swap
                String ibanSwap = transactionDto.getIbanSender();
                transactionDto.setIbanSender(transactionDto.getIbanReceiver());
                transactionDto.setIbanReceiver(ibanSwap);
                break;
        }
        return save(transactionDto);
    }

}
