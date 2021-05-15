package com.ing.tech.bank.resource;

import com.ing.tech.bank.model.dto.TransactionDto;
import com.ing.tech.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionResource {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> save(@RequestBody TransactionDto transaction) {
        return ResponseEntity.ok(transactionService.save(transaction));
    }

    @GetMapping
    public List<TransactionDto> getAll() {
        return transactionService.getAll();
    }

}
