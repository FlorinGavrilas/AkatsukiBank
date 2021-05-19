package com.ing.tech.bank.exceptions;

public class InvalidUserOrPasswordException extends RuntimeException{
    public InvalidUserOrPasswordException(String message) {
        super(message);
    }
}