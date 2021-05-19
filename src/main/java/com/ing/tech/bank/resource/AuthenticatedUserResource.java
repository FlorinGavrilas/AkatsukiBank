package com.ing.tech.bank.resource;

import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.security.JwtTokenUtil;
import com.ing.tech.bank.service.AccountService;
import com.ing.tech.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class AuthenticatedUserResource {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public String sayHello(@PathVariable String username) {
        return "Hello " + username;
    }

    @GetMapping("/accounts")
    public List<AccountDto> getAccounts(@RequestHeader (name = "Authorization") String token) {
        return accountService.getByUsername(jwtTokenUtil.getUsername(token.split(" ")[1]));
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> saveAccount(@RequestHeader (name = "Authorization") String token,
                                                  @RequestBody AccountDto account) {
        return ResponseEntity.ok(accountService.save(account, jwtTokenUtil.getUsername(token.split(" ")[1])));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestHeader (name = "Authorization") String token,
                                                    @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.makeTransaction(transactionDto, "1", jwtTokenUtil.getUsername(token.split(" ")[1])));
    }

    @PostMapping("/request")
    public TransactionDto request(@RequestHeader (name = "Authorization") String token,
                                  @RequestBody TransactionDto transactionDto) {
        return transactionService.makeTransaction(transactionDto, "2", jwtTokenUtil.getUsername(token.split(" ")[1]));
    }


    @GetMapping("/{iban}")
    public List<TransactionDto> getTransactions(@RequestHeader (name = "Authorization") String token,
                                                @PathVariable String iban) {
        return transactionService.getTransactionsByIban(iban, jwtTokenUtil.getUsername(token.split(" ")[1]));
    }

    @GetMapping("/{iban}/request")
    public List<TransactionDto> getRequests(@RequestHeader (name = "Authorization") String token,
                                            @PathVariable String iban) {
        return transactionService.getRequestsByIban(iban, jwtTokenUtil.getUsername(token.split(" ")[1]));
    }

    @GetMapping("/{iban}/income")
    public List<TransactionDto> getIncome(@RequestHeader (name = "Authorization") String token,
                                          @PathVariable String iban) {
        return transactionService.getIncomeByIban(iban, jwtTokenUtil.getUsername(token.split(" ")[1]));
    }

    @GetMapping("/{iban}/outcome")
    public List<TransactionDto> getOutcome(@RequestHeader (name = "Authorization") String token,
                                           @PathVariable String iban) {
        return transactionService.getOutcomeByIban(iban, jwtTokenUtil.getUsername(token.split(" ")[1]));
    }

    @GetMapping("/request/{id}")
    public TransactionDto approveRequest(@RequestHeader (name = "Authorization") String token,
                                               @PathVariable Long id) {
        return transactionService.acceptRequest(id, jwtTokenUtil.getUsername(token.split(" ")[1]));
    }
}
