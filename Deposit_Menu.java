package com.example.banking_system_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class Deposit_Menu {

    @FXML
    private TextField amount;

    @FXML
    private Button done;

    private static BankAccount currentUser;
    public static void setCurrentUser(BankAccount currUser) {
        currentUser = currUser;
    }

    @FXML
    void enter_amount(ActionEvent event) {

    }

    @FXML
    void finish(ActionEvent event) {
        // This method is called when the user clicks the "Done" button
        // You can add code here to finish the deposit process and update the database

        String depositAmountText = amount.getText();

        try {
            double depositAmount = Double.parseDouble(depositAmountText);

            // Update the balance in the database
            updateBalanceInDatabase(currentUser, depositAmount);

            // You might want to update the UI or perform other actions here

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        }
    }

    private void updateBalanceInDatabase(BankAccount customer, double depositAmount) {
            if (customer != null) { // Check if customer is not null
                try (Connection connection = Database.getConnection()) {
                    BankAccount userAccount = customer;

                    if (userAccount != null) {
                        // Update the balance by depositAmount
                        double newBalance = userAccount.getBalance() + depositAmount;
                        userAccount.setBalance(newBalance);

                        // Update the balance in the database
                        Database.deposit(connection, userAccount, depositAmount);

                        System.out.println("Deposit successful. New balance: " + newBalance);
                    } else {
                        System.out.println("User account not found in the database.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error: Customer is null");
            }
    }

}
