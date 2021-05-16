package com.ing.tech.bank.exceptions;

public class SaveAccountException extends RuntimeException{
    public SaveAccountException(String message) {
        super(message);
    }
}