package com.ing.tech.bank.resource;

import com.ing.tech.bank.exceptions.*;
import com.ing.tech.bank.model.dto.RestErrorObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(IbanNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleIbanNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(404).body(new RestErrorObject("Iban not found"));
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<RestErrorObject> handleInsufficientAmount(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(404).body(new RestErrorObject("Not enough funds."));
    }

    @ExceptionHandler(SaveAccountException.class)
    public ResponseEntity<RestErrorObject> handleSaveAccount(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(404).body(new RestErrorObject("Not enough funds."));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorObject> handleUserNotFound(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(404).body(new RestErrorObject("UserNotFound"));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<RestErrorObject> handleTransactionError(Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(404).body(new RestErrorObject("Something went wrong with the transaction."));
    }

}
