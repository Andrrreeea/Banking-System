import org.junit.*;

import static org.junit.Assert.assertEquals;

public class TestConstructor {

    @Test
    public void testBankAccount() {
        Customer customer = new Customer("Teodora", "1234567891011",
                20, "f", "teodora", "teo123");
        BankAccount bankAccount = new BankAccount(customer);
        double delta = 0.0001;
        assertEquals(customer, bankAccount.getCustomer());
        assertEquals(0.0, bankAccount.getBalance(), delta);
    }

    @Test
    public void testCustomer(){
        Customer customer = new Customer("Teodora", "1234567891011",
                20, "f", "teodora", "teo123");
        assertEquals("Teodora", customer.getCustomerName());
        assertEquals("1234567891011", customer.getCNP());
        assertEquals(20, customer.getAge());
        assertEquals("f", customer.getSex());
        assertEquals("teodora", customer.getUsername());
        assertEquals("teo123", customer.getPassword());
    }
}
