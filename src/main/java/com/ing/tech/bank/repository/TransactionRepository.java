package com.ing.tech.bank.repository;

import com.ing.tech.bank.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByIbanReceiverAndType(String ibanReceiver, String type);
    List<Transaction> findTransactionByIbanSenderAndType(String ibanSender, String type);
    @Query(value = "SELECT * FROM Transaction AS t WHERE STRCMP(t.iban_sender, ?1) = 0 OR STRCMP(t.iban_receiver, ?1) = 0", nativeQuery = true)
    List<Transaction> findTransactionByIban(String iban);
}
