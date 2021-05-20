package com.ing.tech.bank.service;

import com.ing.tech.bank.exceptions.IbanNotFoundException;
import com.ing.tech.bank.exceptions.InsufficientAmountException;
import com.ing.tech.bank.exceptions.TransactionException;
import com.ing.tech.bank.logging.InfoLogger;
import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.model.entities.ExchangeRate;
import com.ing.tech.bank.model.entities.Transaction;
import com.ing.tech.bank.repository.TransactionRepository;
import com.ing.tech.bank.security.JwtTokenUtil;
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
    private final JwtTokenUtil jwtTokenUtil;
    private final InfoLogger infoLogger;

    public TransactionDto save(TransactionDto transaction) {
        Transaction transactionEntity = new Transaction(transaction.getIbanSender(), transaction.getIbanReceiver(), transaction.getType(), transaction.getAmount(), LocalDate.now(), transaction.getMessage());
        Transaction savedTransaction = transactionRepository.save(transactionEntity);

        return new TransactionDto(savedTransaction.getId(), savedTransaction.getIbanSender(), savedTransaction.getIbanReceiver(), savedTransaction.getType(), savedTransaction.getAmount(), savedTransaction.getDate(), savedTransaction.getMessage());
    }

    public List<TransactionDto> getAll() {
        return transactionRepository
                .findAll().stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionsByIban(String iban, String username) {

        if (!username.equals(accountService.getByIban(iban).getUsername())) {
            throw new TransactionException("Something went wrong with showing the transactions.");
        }

        return transactionRepository.findTransactionByIban(iban)
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getIncomeByIban(String iban, String username) {

        if (!username.equals(accountService.getByIban(iban).getUsername())) {
            throw new TransactionException("Something went wrong with showing the transactions.");
        }

        return transactionRepository.findTransactionByIbanReceiverAndType(iban, "1")
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getOutcomeByIban(String iban, String username) {

        if (!username.equals(accountService.getByIban(iban).getUsername())) {
            throw new TransactionException("Something went wrong with showing the transactions.");
        }

        return transactionRepository.findTransactionByIbanSenderAndType(iban, "1")
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }

    public boolean validate(TransactionDto transactionDto, String username) {
        AccountDto sender = accountService.getByIban(transactionDto.getIbanSender());

        if (!username.equalsIgnoreCase(sender.getUsername())) {
            throw new IbanNotFoundException("The iban is wrong.");
        }

        if (transactionDto.getIbanSender().equals(transactionDto.getIbanReceiver())) {
            throw new TransactionException("Something went wrong with the transaction.");
        }

        if (Double.compare(sender.getBalance(),transactionDto.getAmount()) < 1) {
            throw new InsufficientAmountException("Not enough money in balance.");
        }
        return true;
    }

    @Transactional
    public void transfer(TransactionDto transactionDto, String username) {
        if (!validate(transactionDto, username)) return;

        AccountDto sender = accountService.getByIban(transactionDto.getIbanSender());
        AccountDto receiver = accountService.getByIban(transactionDto.getIbanReceiver());

        //double receivedAmount = transactionDto.getAmount() * currencyService.getConversionRate(sender.getCurrency(), receiver.getCurrency());
        double receivedAmount = transactionDto.getAmount() * ExchangeRate.getRate(sender.getCurrency(), receiver.getCurrency());

        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        receiver.setBalance(receiver.getBalance() + receivedAmount);

        accountService.save(sender, sender.getUsername());
        accountService.save(receiver, receiver.getUsername());
    }

    public TransactionDto makeTransaction(TransactionDto transactionDto, String type, String username) {
        transactionDto.setType(type);

        switch (transactionDto.getType()) {
            case "1":
                transfer(transactionDto, username);
                break;
            case "2":
                //make swap
                validateRequest(transactionDto, username);
                transactionDto.setAmount(transactionDto.getAmount() * currencyService.getConversionRate(accountService.getByIban(transactionDto.getIbanReceiver()).getCurrency(), accountService.getByIban(transactionDto.getIbanSender()).getCurrency()));
                String ibanSwap = transactionDto.getIbanSender();
                transactionDto.setIbanSender(transactionDto.getIbanReceiver());
                transactionDto.setIbanReceiver(ibanSwap);
                break;
        }
        return save(transactionDto);
    }

    private boolean validateRequest(TransactionDto transactionDto, String username) {
        AccountDto sender = accountService.getByIban(transactionDto.getIbanSender());

        if (!username.equalsIgnoreCase(sender.getUsername())) {
            throw new IbanNotFoundException("The iban is wrong.");
        }

        if (transactionDto.getIbanSender().equals(transactionDto.getIbanReceiver())) {
            throw new TransactionException("Something went wrong with the transaction.");
        }

        return true;
    }

    public TransactionDto acceptRequest(Long id, String username) {
        Transaction transactionEntity = transactionRepository.findTransactionById(id);

        if (!"2".equals(transactionEntity.getType())) {
            throw new TransactionException("This is not a request.");
        }

        TransactionDto savedTransaction = new TransactionDto(transactionEntity.getId(),
                                                            transactionEntity.getIbanSender(),
                                                            transactionEntity.getIbanReceiver(),
                                                            transactionEntity.getType(),
                                                            transactionEntity.getAmount(),
                                                            transactionEntity.getDate(),
                                                            transactionEntity.getMessage());
        transactionRepository.delete(transactionEntity);
        return makeTransaction(savedTransaction, "1", username);
    }

    public List<TransactionDto> getRequestsByIban(String iban, String username) {
        if (!username.equals(accountService.getByIban(iban).getUsername())) {
            throw new TransactionException("Something went wrong with showing the transactions.");
        }

        return transactionRepository.findTransactionByIbanSenderAndType(iban, "2")
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getIbanSender(),
                        transaction.getIbanReceiver(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage())
                )
                .collect(Collectors.toList());
    }
}
