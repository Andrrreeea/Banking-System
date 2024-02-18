package com.example.banking_system_fx;
import java.util.Scanner;
import java.util.Set;

public class Utilities {
    public static String readInput() {
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        } catch (RuntimeException error) {
            System.out.println(error.getMessage());
            return null;
        }
    }

    public static BankAccount searchBankAccountByCustomerCNP(Set<BankAccount> bankAccounts) {
        System.out.println("Please enter in the CNP: ");
        String cnp = readInput();
        if (cnp == null) return null;

        for (BankAccount bankAccount : bankAccounts) {
            Customer customer = bankAccount.getCustomer();
            if (customer.getCNP().equals(cnp)) return bankAccount;
        }

        return null;
    }
}
