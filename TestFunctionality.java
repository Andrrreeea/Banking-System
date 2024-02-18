import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import static org.junit.Assert.*;

public class TestFunctionality {
    @Test
    public void depositShouldIncreaseBalance() {
        BankAccount bankAccount;
        Customer customer = new Customer("Teodora", "1234567890123", 20,
                "f", "teodora", "teo123");
        bankAccount = new BankAccount(customer);
        double initialBalance = bankAccount.getBalance();
        double depositAmount = 50.0;

        bankAccount.deposit(depositAmount);

        double expectedBalance = initialBalance + depositAmount;
        assertEquals(expectedBalance, bankAccount.getBalance(), 0.0001);
    }

    @Test
    public void withdrawShouldDecreaseBalance() {
        BankAccount bankAccount;
        Customer customer = new Customer("Teodora", "1234567890123", 20,
                "f", "teodora", "teo123");
        bankAccount = new BankAccount(customer);
        double initialBalance = 100.0;
        bankAccount.deposit(initialBalance);
        double withdrawAmount = 30.0;

        bankAccount.withdraw(withdrawAmount);

        double expectedBalance = initialBalance - withdrawAmount;
        assertEquals(expectedBalance, bankAccount.getBalance(), 0.0001);
    }

    @Test
    public void TestList() {
        Set<BankAccount> bankAccounts = new HashSet<>();
        Customer customer1 = new Customer("John Doe", "1234567890123", 30,
                "m","jane","jane");
        BankAccount bankAccount1 = new BankAccount(customer1);

        Customer customer2 = new Customer("Jane Doe", "9876543210987", 25,
                "f","janeta","janeta123");
        BankAccount bankAccount2 = new BankAccount(customer2);

        bankAccounts.add(bankAccount1);
        bankAccounts.add(bankAccount2);

        String simulatedInput = "9876543210987\n";
        InputStream originalSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {

            BankAccount foundBankAccount = Utilities.searchBankAccountByCustomerCNP(bankAccounts);

            assertNotNull(foundBankAccount);
            assertEquals("9876543210987", foundBankAccount.getCustomer().getCNP());
        } finally {
            System.setIn(originalSystemIn);
        }
    }
}
