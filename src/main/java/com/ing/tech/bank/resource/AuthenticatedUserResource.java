package com.ing.tech.bank.resource;

import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.service.AccountService;
import com.ing.tech.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{username}")
@AllArgsConstructor
public class AuthenticatedUserResource {
    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
    public String sayHello(@PathVariable String username) {
        return "Hello " + username;
    }

    @GetMapping("/accounts")
    public List<AccountDto> getAccounts(@PathVariable String username) {
        return accountService.getByUsername(username);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.makeTransaction(transactionDto, "1"));
    }

    @PostMapping("/request")
    public TransactionDto request(@RequestBody TransactionDto transactionDto) {
        return transactionService.makeTransaction(transactionDto, "2");
    }

    @PostMapping("/exchange")
    public TransactionDto exchange(@RequestBody TransactionDto transactionDto) {
        return transactionService.makeTransaction(transactionDto, "3");
    }

    @GetMapping("/{iban}")
    public List<TransactionDto> getTransactions(@PathVariable String iban) {
        return transactionService.getTransactionsByIban(iban);
    }

    @GetMapping("/{iban}/income")
    public List<TransactionDto> getIncome(@PathVariable String iban) {
        return transactionService.getIncomeByIban(iban);
    }

    @GetMapping("/{iban}/outcome")
    public List<TransactionDto> getOutcome(@PathVariable String iban) {
        return transactionService.getOutcomeByIban(iban);
    }
}
