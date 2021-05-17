package com.ing.tech.bank.service;

import com.ing.tech.bank.exceptions.IbanNotFoundException;
import com.ing.tech.bank.logging.InfoLogger;
import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.entities.Account;
import com.ing.tech.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final InfoLogger infoLogger;

    public AccountDto save(AccountDto account, String username) {

        //TODO update save so that it will update an account if there is already one accoutn with the same account.
//        Account accountEntity = new Account(username, account.getIban(), account.getBalance(), account.getCurrency());
//        Account savedAccount = accountRepository.save(accountEntity);

        Account accountEntity;

        Optional<Account> currentAccount = accountRepository.findAccountByIban(account.getIban());

        if (currentAccount.isPresent()) {
            accountEntity = currentAccount.get();
            accountEntity.setBalance(account.getBalance());
        } else {
            accountEntity = new Account(username, account.getIban(), account.getBalance(), account.getCurrency());
        }

        Account savedAccount = accountRepository.save(accountEntity);

        return new AccountDto(savedAccount.getUsername(), savedAccount.getIban(), savedAccount.getBalance(), account.getCurrency());
    }

    public List<AccountDto> getAll() {
        return accountRepository
                .findAll().stream()
                .map(account -> new AccountDto(
                        account.getUsername(),
                        account.getIban(),
                        account.getBalance(),
                        account.getCurrency())
                )
                .collect(Collectors.toList());
    }

    public List<AccountDto> getByUsername(String username) {
        return accountRepository.findAccountByUsername(username)
                .stream()
                .map(account -> new AccountDto(
                        account.getUsername(),
                        account.getIban(),
                        account.getBalance(),
                        account.getCurrency())
                )
                .collect(Collectors.toList());
    }

    public AccountDto getByIban(String iban) {
        Account accountEntity = accountRepository.findAccountByIban(iban).orElseThrow(() -> new IbanNotFoundException("Iban not found."));
        Account savedAccount = accountRepository.save(accountEntity);

        return new AccountDto(savedAccount.getUsername(), savedAccount.getIban(), savedAccount.getBalance(), savedAccount.getCurrency());
    }
}
