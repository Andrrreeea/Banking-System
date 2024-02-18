package com.example.banking_system_fx;
public class Deposit extends Transaction {
    public Deposit(double balance, double amount, Customer initiator) {
        super(balance, amount, initiator);
    }

    public void deposit(double amount) {
        System.out.printf("Depositing $%f in %s's account.\n", amount, super.initiator.getCustomerName());
        super.balance += this.amount;
    }

    @Override
    public String toString() {
        return String.format("[%s]: Transaction DEPOSIT: %f deposited in bank account %s. Balance at the time: %f",
                this.date.toString(), amount, super.initiator.getCNP(), balance);
    }
}