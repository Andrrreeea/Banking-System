package com.example.banking_system_fx;
import java.util.ArrayList;

public class BankAccount implements TransactionPerformer, BalanceHolder {
    private final Customer customer;
    private int accountID;
    private final ArrayList<Transaction> transactions = new ArrayList<>(1);
    private double balance;

    public BankAccount(Customer customer) {
        this.customer = customer;
        this.balance = 0.0;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void deposit(double amount) {
        Deposit depositTransaction = new Deposit(balance, amount, customer);
        depositTransaction.deposit(amount);
        this.balance = depositTransaction.balance;

        System.out.println("Deposit of $" + amount + " successful. Current balance: $" + balance);
        transactions.add(depositTransaction);
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public void withdraw(double amount) {
        Withdraw withdrawTransaction = new Withdraw(balance, amount, customer);
        try {
            withdrawTransaction.withdraw(amount);
        } catch (InsufficientFundsException error) {
            System.out.println(error.getMessage());
            return;
        }

        this.balance = withdrawTransaction.balance;
        System.out.println("Withdraw of $" + amount + " successful. Current balance: $" + balance);
        transactions.add(withdrawTransaction);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public int compareTo(BankAccount otherAccount) {
        return Double.compare(this.balance, otherAccount.balance);
    }

    public void showTransactions() {
        System.out.printf("Bank account for customer with CNP %s has the following transactions registered: \n", customer.getCNP());
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}


