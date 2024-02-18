package com.example.banking_system_fx;
public class Withdraw extends Transaction {
    public Withdraw(double balance, double amount, Customer initiator) {
        super(balance, amount, initiator);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        System.out.printf("Withdrawing $%f in %s's account.\n", amount, super.initiator.getCustomerName());
        if (super.balance < this.amount)
            throw new InsufficientFundsException(
                    String.format("Bank account for customer %s has insufficient funds.\n", super.initiator.getCustomerName())
            );
        super.balance -= this.amount;
    }

    @Override
    public String toString() {
        return String.format("[%s]: Transaction WITHDRAW: %f deposited in bank account %s. Balance at the time: %f",
                this.date.toString(), amount, super.initiator.getCNP(), balance);
    }
}
