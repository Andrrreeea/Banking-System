package com.example.banking_system_fx;
import java.util.Date;

public abstract class Transaction implements Comparable<Transaction>{
    protected double amount;
    protected double balance;
    protected Customer initiator;

    protected Date date;

    public Transaction(double balance, double amount, Customer initiator) {
        this.balance = balance;
        this.amount = amount;
        this.initiator = initiator;
        this.date = new Date();
    }

    public Customer getInitiator() {
        return initiator;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public int compareTo(Transaction otherTransaction) {
        return (int)(this.amount - otherTransaction.amount);
    }
}

