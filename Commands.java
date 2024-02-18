package com.example.banking_system_fx;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Commands {

    private static Double extractNumberFromString() {
        String rawAmount = Utilities.readInput();
        if (rawAmount == null) return null;

        double amount;
        try {
            amount = Double.parseDouble(rawAmount);
        } catch(NumberFormatException error) {
            System.out.println("That is not a valid number.");
            return null;
        }
        return amount;
    }

    public static void deposit(BankAccount bankAccount) {
        System.out.print("Enter amount: ");
        Double amount = extractNumberFromString();
        if (amount == null) return;

        // Open the connection before performing the deposit operation
        Connection connection = Database.getConnection();
        bankAccount.deposit(amount);
        Database.deposit(connection, bankAccount, amount);
    }


    public static void exit() {
        Database.closeConnection();
        System.out.println("Good bye!");
    }

    public static void withdraw(BankAccount bankAccount) {
        System.out.print("Enter amount: ");
        Double amount = extractNumberFromString();
        if (amount == null) return;
        Connection connection = Database.getConnection();
        bankAccount.withdraw(amount);
        Database.withdraw(connection, bankAccount, amount);
    }

    public static void balance(BankAccount bankAccount) {
        System.out.printf("Balance for customer with CNP %s: %f\n", bankAccount.getCustomer().getCNP(), bankAccount.getBalance());
    }

    public static void sort(Set<BankAccount> bankAccounts) {
        System.out.print("What do you want to sort by? ");
        String sortingTarget = Utilities.readInput();
        if (sortingTarget == null) return;

        if (sortingTarget.equalsIgnoreCase("customers")) {
            BankAccount[] bankAccountsSorted = sortByCustomers(bankAccounts);
            for (BankAccount bankAccount : bankAccountsSorted)
                System.out.printf("Customer %s with balance $%f\n", bankAccount.getCustomer().getCNP(), bankAccount.getBalance());

        } else if (sortingTarget.equalsIgnoreCase("transaction")) {
            System.out.print("What is the CNP for the customer? ");
            BankAccount foundBankAccount = Utilities.searchBankAccountByCustomerCNP(bankAccounts);
            if (foundBankAccount == null) {
                System.out.println("That CNP does not belong to a customer.");
                return;
            }

            ArrayList<Transaction> transactionsSorted = sortByTransactionsForCustomer(foundBankAccount);
            for (Transaction transactionSorted : transactionsSorted)
                System.out.println(transactionSorted);
        }
    }

    public static void balance(BankAccount bankAccount, String username, String role) {
        if ("admin".equalsIgnoreCase(role)) {
            // Admin balance display logic (display balances for all users)
            displayAllUserBalances();
        } else {
            // Regular user balance display logic
            double balance = Database.getBalance(username, role);
            if (balance >= 0) {
                System.out.println("Balance: $" + balance);
            } else {
                System.out.println("Failed to retrieve balance.");
            }
        }
    }

    private static void displayAllUserBalances() {
        // Retrieve all user balances and display them
        // You need to implement a method in the Database class to retrieve all user balances
        // For example, you can use a SQL query to fetch all balances from the accounts table
        List<Double> allBalances = Database.getAllUserBalances();

        if (allBalances != null) {
            for (int i = 0; i < allBalances.size(); i++) {
                System.out.printf("User %d Balance: $%f\n", i + 1, allBalances.get(i));
            }
        } else {
            System.out.println("Failed to retrieve user balances.");
        }
    }


    public static BankAccount[] sortByCustomers(Set<BankAccount> bankAccounts) {
        BankAccount[] bankAccountsArray = bankAccounts.toArray(BankAccount[]::new);
        Arrays.sort(bankAccountsArray, BankAccount::compareTo);
        return bankAccountsArray;
    }

    public static ArrayList<Transaction> sortByTransactionsForCustomer(BankAccount bankAccount) {
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) bankAccount.getTransactions().clone();

        transactions.sort(Transaction::compareTo);
        return transactions;
    }

    public static void list(Set<BankAccount> bankAccounts) {
        BankAccount foundBankAccount = Utilities.searchBankAccountByCustomerCNP(bankAccounts);
        if (foundBankAccount == null) return;

        foundBankAccount.showTransactions();
    }
}
