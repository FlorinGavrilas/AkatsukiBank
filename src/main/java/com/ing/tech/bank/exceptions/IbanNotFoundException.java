package com.ing.tech.bank.exceptions;

public class IbanNotFoundException extends RuntimeException{
    public IbanNotFoundException(String message) {
        super(message);
    }
}
