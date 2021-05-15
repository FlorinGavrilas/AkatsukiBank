package com.ing.tech.bank.resource;

import com.ing.tech.bank.model.dto.AccountDto;
import com.ing.tech.bank.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountResource {
    private final AccountService accountService;

    @PostMapping("/{username}")
    public ResponseEntity<AccountDto> save(@RequestBody AccountDto account, @PathVariable String username) {
        return ResponseEntity.ok(accountService.save(account, username));
    }

    @GetMapping("/{username}")
    public List<AccountDto> getByUsername(@PathVariable String username) {
        return accountService.getByUsername(username);
    }

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }
}
