package com.ing.tech.bank.service;

import com.ing.tech.bank.exceptions.InsufficientAmountException;
import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.model.entities.Transaction;
import com.ing.tech.bank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public TransactionDto save(TransactionDto transaction) {
        Transaction transactionEntity = new Transaction(transaction.getIbanSender(), transaction.getIbanReceiver(), transaction.getType(), transaction.getAmount(), LocalDate.now(), transaction.getMessage());
        Transaction savedTransaction = transactionRepository.save(transactionEntity);

        //TODO to be changed with makeTransaction() after the logic is implemented
        transfer(savedTransaction);

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

    public boolean validate(Transaction transaction) {
        AccountDto sender = accountService.getByIban(transaction.getIbanSender());

        if (Double.compare(sender.getBalance(),transaction.getAmount()) < 1) {
            throw new InsufficientAmountException("Not enough money in balance.");
        }
        return true;
    }

    @Transactional
    public boolean transfer(Transaction transaction) {
        if (!validate(transaction)) return false;

        AccountDto sender = accountService.getByIban(transaction.getIbanSender());
        AccountDto receiver = accountService.getByIban(transaction.getIbanReceiver());

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());

        accountService.save(sender, sender.getUsername());
        accountService.save(receiver, receiver.getUsername());

        return true;
    }

    public void makeTransaction(Transaction transaction) {
        //TODO this will call transfer() based on the type of the transaction.
    }
}
