package com.example.banking_system_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class Log_In_Controller {

    @FXML
    private ToggleGroup MenuButton;

    @FXML
    private RadioMenuItem admin;

    @FXML
    private Button log_in;

    @FXML
    private MenuButton menu;

    @FXML
    private PasswordField password;

    @FXML
    private RadioMenuItem user;

    @FXML
    private TextField username;

    @FXML
    void admin(ActionEvent event) {

    }

    @FXML
    void choose(ActionEvent event) {

    }

    @FXML
    void enterPassword(ActionEvent event) {

    }

    @FXML
    void enterUsername(ActionEvent event) {

    }

    @FXML
    void final_login(ActionEvent event) throws IOException{
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        String selectedRole = admin.isSelected() ? "admin" : "user";

        if (Database.validateUserCredentials(enteredUsername, enteredPassword, selectedRole)) {
            // Credentials are valid, you can proceed with the login logic
            System.out.println("Login successful");
            Deposit_Menu.setCurrentUser(Database.getAccountByUsername(Database.getConnection(),enteredUsername));
            // You can retrieve the balance or perform other actions based on the role
            double balance = Database.getBalance(enteredUsername, selectedRole);
            System.out.println("Balance: " + balance);
        } else {
            // Credentials are invalid, you can show an error message or take appropriate action
            System.out.println("Login failed");
        }
        if ("user".equals(selectedRole)) {
            Stage stage= (Stage) log_in.getScene().getWindow();
            Parent root= FXMLLoader.load(getClass().getResource("menu_user.fxml"));
            Scene scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if ("admin".equals(selectedRole)) {
            Stage stage= (Stage) log_in.getScene().getWindow();
            Parent root= FXMLLoader.load(getClass().getResource("menu_admin.fxml"));
            Scene scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Unknown role: " + selectedRole);
        }

    }

    @FXML
    void user(ActionEvent event) {

    }

}
