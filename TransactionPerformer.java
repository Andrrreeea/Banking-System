package com.example.banking_system_fx;
public interface TransactionPerformer {

    void deposit(double amount);
    void withdraw(double amount) throws InsufficientFundsException;
}
