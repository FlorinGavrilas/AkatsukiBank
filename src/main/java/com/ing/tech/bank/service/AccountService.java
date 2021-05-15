package com.ing.tech.bank.service;

import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.entities.Account;
import com.ing.tech.bank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountDto save(AccountDto account, String username) {
        Account accountEntity = new Account(username, account.getIban(), account.getBalance(), account.getCurrency());
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
}
