<h1 style="text-align: center; color: #ffffff;">Banking System</h1>

# Description

The provided Java application is a text-based banking system that allows users to sign up, log in, and perform various banking operations. Below is an overview of the key features, challenges faced during development, and potential future enhancements.

## Functionality

### Sign Up (Customer Registration)

Users can sign up by providing their name, CNP (personal identification number), age, and gender. The system ensures valid inputs and prevents the creation of duplicate customers with the same CNP.

### Login (Customer Authentication)

Users can log in by entering their CNP. If the customer does not exist, the system prompts the user to sign up.

### Bank Account Operations

Once logged in, customers can perform various operations on their bank account, including:

- Deposit money
- Withdraw money
- Check account balance
- View transaction history
- View a list of all the costumers ordered by balance in their account

### Data Persistence

Customer and bank account data is stored in a file (`data.txt`) for persistence. Data is loaded when the application starts and saved when the user exits.

### Command-Line Interface (CLI)

The system interacts with users through a simple command-line interface. Users input instructions (commands) to perform different operations.

### Sorting and Listing

Users can sort bank accounts by customers or view the transactions of a specific customer. Sorting is based on the balance of bank accounts.

### Exception Handling

Custom exceptions (`ExistingCustomerException` and `InsufficientFundsException`) are used for handling specific scenarios, enhancing error handling practices.

### Object-Oriented Design (OOD)

The code follows object-oriented design principles with classes like `BankAccount`, `Customer`, `Transaction`, `Application`, `Commands`, `Database`, `Deposit`, `Withdraw`, `Utilities` and interfaces like `TransactionPerformer` and `BalanceHolder`.

## Technology Used
- Java

## Achievements
- Demonstrated a deep understanding of object-oriented design principles
- Showcased strong error-handling practices by implementing custom exceptions to handle specific scenarios effectively
- Accomplished the implementation of data persistence through the Database class, showcasing the ability to read and write data to a file

## Future Features

- **Enhanced Security:** Implement additional security features such as password protection or encryption for sensitive customer information.
- **Graphical User Interface (GUI):** Develop a graphical user interface to improve user experience and make the application more accessible.
- **Account Types and Transactions:** Introduce different types of bank accounts (e.g., savings, checking) and support more complex transaction types.
- **Multi-User Support:** Extend the application to support multiple users simultaneously, possibly with user authentication.
- **Transaction Categorization:** Implement a system for categorizing transactions (e.g., expenses, income) and providing detailed transaction reports.
- **Automated Tests:** Introduce automated testing to ensure the reliability and stability of the application, especially when adding new features.

Overall, the application serves as a foundation for a basic banking system and provides opportunities for further refinement and expansion. The challenges faced during development highlight the importance of robust design and careful consideration of user interactions and data management.

import java.io.IOException;
import java.util.*;

public class Application {
private final Set<BankAccount> bankAccounts = new HashSet<>();
private final Database database = new Database("C:\\Users\\andre\\IdeaProjects\\banking system\\src\\data.txt");

    private Customer signup() throws ExistingCustomerException {
        String name, cnp, sex;
        int age;

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
        return new Customer(name, cnp, age, sex);
    }


    private Customer login() {
        BankAccount foundBankAccount = Utilities.searchBankAccountByCustomerCNP(bankAccounts);
        if (foundBankAccount == null) {
            System.out.println("The customer with this CNP does not exist. Sign up! >:(");
            try {
                return signup();
            } catch (ExistingCustomerException error) {
                System.err.println(error.getMessage());
                return null;
            }
        }

        return foundBankAccount.getCustomer();
    }

    private void saveData() {
        try {
            List<String> dataLines = new ArrayList<>();
            for (BankAccount bankAccount : this.bankAccounts) {
                dataLines.add(bankAccount.getCustomer().toString());
            }
            database.writeLines(dataLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            List<String> dataLines = database.readLines();

            for (String dataLine : dataLines) {
                BankAccount bankAccount = parseBankAccount(dataLine);
                if (bankAccount != null) {
                    bankAccounts.add(bankAccount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BankAccount parseBankAccount(String dataLine) {
        String[] parts = dataLine.split(",");

        if (parts.length == 4) {
            String customerName = parts[0].trim();
            String customerCNP = parts[1].trim();
            int customerAge = Integer.parseInt(parts[2].trim());
            String customerSex = parts[3].trim();

            Customer customer = new Customer(customerName, customerCNP, customerAge, customerSex);
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

        Customer currentCustomer;
        BankAccount currentBankAccount = null;
        if (args[0].equals("signup")) {
            try {
                currentCustomer = signup();
            } catch (ExistingCustomerException error) {
                System.err.println(error.getMessage());
                return;
            }

            if (currentCustomer == null) return;
            currentBankAccount = new BankAccount(currentCustomer);
            bankAccounts.add(currentBankAccount);
        } else if (args[0].equals("login")) {
            currentCustomer = login();
            if (currentCustomer == null) return;

            for (BankAccount bankAccount : bankAccounts) {
                if (bankAccount.getCustomer().getCNP().equals(currentCustomer.getCNP())) {
                    currentBankAccount = bankAccount;
                    break;
                }
            }

            if (currentBankAccount == null) {
                System.out.println("This customer does not have an associated bank account. Creating one.");
                currentBankAccount = new BankAccount(currentCustomer);
            }
        } else {
            System.out.println("You can only sign up or log in.");
            return;
        }

         System.out.println("""
                 Instructions:\n\tType 'deposit' to deposit money,
                 \t'withdraw' to withdraw money,
                 \t'balance' to check balance,
                 \t'sort' to sort by customers, or transactions of a customer,
                 \t'show' to show the transactions of a customer,
                 \tor 'exit' to quit.""");

        while (true) {
            System.out.print("Enter your instruction: ");
            String instruction = Utilities.readInput();
            if (instruction == null) continue;

            switch(instruction.toLowerCase()) {
                case "deposit" -> Commands.deposit(currentBankAccount);
                case "withdraw" -> Commands.withdraw(currentBankAccount);
                case "balance" -> Commands.balance(currentBankAccount);
                case "sort" -> Commands.sort(bankAccounts);
                case "show" -> Commands.list(bankAccounts);
                case "exit" -> {
                    Commands.exit();
                    saveData();
                    return;
                }
                default ->
                        System.out.println("Invalid instruction. Please enter 'deposit', 'withdraw', 'balance', or 'exit'.");
            }
        }
    }
}
public interface BalanceHolder {
double getBalance();
}
import java.util.ArrayList;

public class BankAccount implements TransactionPerformer, BalanceHolder {
private final Customer customer;

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


import java.util.ArrayList;
import java.util.Arrays;
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

        bankAccount.deposit(amount);
    }

    public static void exit() {
        System.out.println("Good bye!");
    }

    public static void withdraw(BankAccount bankAccount) {
        System.out.print("Enter amount: ");
        Double amount = extractNumberFromString();
        if (amount == null) return;

        bankAccount.withdraw(amount);
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
public class Customer  {
private final String name, cnp;
private final int age;
private final String sex;

    public Customer(String name, String cnp, int age, String sex) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
        this.sex = sex;
    }

    public String getCNP() {
        return cnp;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getCustomerName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.cnp + ", " + this.age + ", " + this.sex;
    }
}
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Database {
private final String path;

    public Database(String path) {
        this.path = path;
    }

    public List<String> readLines() throws IOException {
        return Files.readAllLines(Path.of(this.path));
    }

    public void writeLines(List<String> lines) throws IOException {
        Files.write(Path.of(this.path), lines);
    }
}
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
}public class ExistingCustomerException extends Throwable {
public ExistingCustomerException(String message) {
super(message);
}
}
public class InsufficientFundsException extends Throwable {
public InsufficientFundsException(String message) {
super(message);
}
}
public class Main {
public static void main(String[] args) {
Application bankingApp = new Application();
bankingApp.run(args);
}
}import java.util.Date;

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

    @Override
    public int compareTo(Transaction otherTransaction) {
        return (int)(this.amount - otherTransaction.amount);
    }
}
public interface TransactionPerformer {

    void deposit(double amount);
    void withdraw(double amount) throws InsufficientFundsException;
}
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