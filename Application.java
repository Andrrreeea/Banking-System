package com.example.banking_system_fx;
import java.io.IOException;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    private final Set<BankAccount> bankAccounts = new HashSet<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    private Customer signup() throws ExistingCustomerException {
        Database.getConnection();
        String name, cnp, sex;
        int age;
        boolean exists=true;
        String username;
        do {
            System.out.print("Enter username for customer: ");
            username = Utilities.readInput();
            exists = Database.isUsernameExists(Database.getConnection(), username);
            if (exists) {
                System.out.println("Username already exists.");
            }
        }while(exists==true);
        if (username == null) return null;
        if (username.isEmpty()) {
            System.out.println("You have to specify a username.");
            return null;
        }

        System.out.print("Enter password for customer: ");
        String password = Utilities.readInput();
        if (password == null) return null;
        if (password.isEmpty()) {
            System.out.println("You have to specify a password.");
            return null;
        }

        System.out.print("Enter name for customer: ");
        name = Utilities.readInput();
        if (name == null) return null;
        if (name.isEmpty()) {
            System.out.println("You have to specify a name.");
            return null;
        }

        System.out.print("Enter CNP for customer: ");
        cnp = Utilities.readInput();
        if (cnp == null) return null;
        if (cnp.length() != 13) {
            System.out.println("The CNP you've entered is incorrect.");
            return null;
        }

        System.out.print("Enter age for customer: ");
        try {
            String rawAge = Utilities.readInput();
            if (rawAge == null) return null;
            age = Integer.parseInt(rawAge);
        } catch (NumberFormatException error) {
            System.out.println("That is not a valid number.");
            return null;
        }

        if (age < 18) {
            System.out.println("You're not old enough to have a bank account.");
            return null;
        }

        System.out.print("Enter customer's sex (m/f): ");
        sex = Utilities.readInput();
        if (sex == null) return null;
        if (!sex.equalsIgnoreCase("m") && !sex.equalsIgnoreCase("f")) {
            System.out.println("Customer's sex can only be in this format: \"m\" or \"f\"");
            return null;
        }

        for (BankAccount bankAccount : bankAccounts) {
            if (bankAccount.getCustomer().getCNP().equals(cnp))
                throw new ExistingCustomerException("The customer with this CNP already exists.");
        }
        System.out.print("Enter role for customer (user/admin): ");
        String role = Utilities.readInput();
        if (role == null) return null;

        Customer customer = new Customer(name, cnp, age, sex, username, password, role.toLowerCase());
        Database.addUserToDatabase(customer);
        return customer;

    }

    public Customer login() {
        System.out.print("Enter username: ");
        String username = Utilities.readInput();
        if (username == null) return null;

        System.out.print("Enter password: ");
        String password = Utilities.readInput();
        if (password == null) return null;

        System.out.print("Enter role (user/admin): ");
        String role = Utilities.readInput();
        if (role == null) return null;

        if (Database.validateUserCredentials(username, password, role.toLowerCase())) {
            return new Customer(username, password, role.toLowerCase());
        } else {
            System.out.println("Invalid username, password, or role. Please try again.");
            return null;
        }
    }

    private void saveData() {
      /*  try {
            List<String> dataLines = new ArrayList<>();
            for (BankAccount bankAccount : this.bankAccounts) {
                dataLines.add(bankAccount.getCustomer().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void loadData() {
       /* try {
            for (String dataLine : dataLines) {
                BankAccount bankAccount = parseBankAccount(dataLine);
                if (bankAccount != null) {
                    bankAccounts.add(bankAccount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private BankAccount parseBankAccount(String dataLine) {
        String[] parts = dataLine.split(",");

        if (parts.length == 4) {
            String customerName = parts[0].trim();
            String customerCNP = parts[1].trim();
            int customerAge = Integer.parseInt(parts[2].trim());
            String customerSex = parts[3].trim();
            String customerUsername = parts[4].trim();
            String customerPassword = parts[5].trim();

            Customer customer = new Customer(customerName, customerCNP, customerAge, customerSex, customerUsername, customerPassword);
            return new BankAccount(customer);
        } else {
            System.err.println("Error parsing data line: " + dataLine);
            return null;
        }
    }

    public void run(String[] args) {
        loadData();

        if (args.length == 0) {
            System.out.println("No arguments supplied.");
            return;
        }

        if (args[0].equals("signup")) {
            try {
                Customer currentCustomer = signup();
                if (currentCustomer == null) return;

                BankAccount currentBankAccount = new BankAccount(currentCustomer);
                bankAccounts.add(currentBankAccount);

                BankingThread bankingThread = new BankingThread(currentBankAccount);
                executorService.submit(bankingThread);
            } catch (ExistingCustomerException error) {
                System.err.println(error.getMessage());
            }
        } else if (args[0].equals("login")) {
            Customer currentCustomer = login();
            if (currentCustomer == null) return;
            Connection connection = Database.getConnection();
            BankAccount bankAccount=Database.getAccountByUsername(connection,currentCustomer.getUsername());

            if (bankAccount == null) {
                System.out.println("This customer does not have an associated bank account. Creating one.");
               bankAccount = new BankAccount(currentCustomer);
                bankAccounts.add(bankAccount);
            }

            BankingThread bankingThread = new BankingThread(bankAccount);
            executorService.submit(bankingThread);
        } else {
            System.out.println("You can only sign up or log in.");
        }
    }

    private class BankingThread extends Thread {
        private final BankAccount bankAccount;

        public BankingThread(BankAccount bankAccount) {
            this.bankAccount = bankAccount;
        }

        @Override
        public void run() {
            System.out.println("Thread started for customer with CNP: " + bankAccount.getCustomer().getCNP());

            System.out.println("""
                    Instructions:\n\tType 'deposit' to deposit money,
                    \t'withdraw' to withdraw money,
                    \t'balance' to check balance,
                    \t'sort' to sort by customers or transactions of a customer,
                    \t'show' to show the transactions of a customer,
                    \tor 'exit' to quit.""");

            while (true) {
                System.out.print("Enter your instruction: ");
                String instruction = Utilities.readInput();
                if (instruction == null) continue;

                try {
                    switch (instruction.toLowerCase()) {
                        case "deposit" -> Commands.deposit(bankAccount);
                        case "withdraw" -> Commands.withdraw(bankAccount);
                        case "balance" -> Commands.balance(bankAccount);
                        case "sort" -> Commands.sort(bankAccounts);
                        case "show" -> Commands.list(bankAccounts);
                        case "admin balance" -> Commands.balance(bankAccount, bankAccount.getCustomer().getUsername(), bankAccount.getCustomer().getRole());
                        case "exit" -> {
                            Commands.exit();
                            saveData();
                            executorService.shutdown();
                            return;
                        }
                        default ->
                                System.out.println("Invalid instruction. Please enter 'deposit', 'withdraw', 'balance', or 'exit'.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
