package com.ing.tech.bank.service;

import com.ing.tech.bank.exceptions.IbanNotFoundException;
import com.ing.tech.bank.logging.InfoLogger;
import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.entities.Account;
import com.ing.tech.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final InfoLogger infoLogger;

    public AccountDto save(AccountDto account, String username) {

        Account accountEntity;

        Optional<Account> currentAccount = accountRepository.findAccountByIban(account.getIban());
        if (currentAccount.isPresent()) {
            if (!username.equals(currentAccount.get().getUsername())) {
                throw new IbanNotFoundException("Account could not be created.");
            }

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
