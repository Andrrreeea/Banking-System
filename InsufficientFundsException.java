package com.example.banking_system_fx;
public class InsufficientFundsException extends Throwable {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
