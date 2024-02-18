package com.example.banking_system_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Sign_Up_Controller {

    @FXML
    private TextField age_field;

    @FXML
    private TextField cnp_field;

    @FXML
    private ToggleGroup gender;

    @FXML
    private TextField name_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private ToggleGroup role;

    @FXML
    private Button sign_up;

    @FXML
    private TextField user_field;

    @FXML
    void final_signup(ActionEvent event) throws IOException {
        String username = user_field.getText();
        String password = pass_field.getText();
        String name = name_field.getText();
        String cnp = cnp_field.getText();
        String ageStr = age_field.getText();
        String genderSelection = getSelectedRadioButtonValue(gender);
        String roleSelection = getSelectedRadioButtonValue(role);

        try {
            int age = Integer.parseInt(ageStr);
            if (cnp.length() != 13) {
                System.out.println("Invalid CNP length. Please enter a 13-character CNP.");
                return;  // Exit the method if CNP length is not valid
            }
            // Create a Customer object
            Customer customer = new Customer(name, cnp, age, genderSelection, username, password, roleSelection);
            System.out.println(roleSelection);
            // Add the customer to the database
            Database.addUserToDatabase(customer);
            System.out.println("User added to the database.");

            // You can add further actions or UI updates here

        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Please enter a valid number.");
        }

        if ("user".equals(roleSelection)) {
            Stage stage= (Stage) sign_up.getScene().getWindow();
            Parent root= FXMLLoader.load(getClass().getResource("menu_user.fxml"));
            Scene scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if ("admin".equals(roleSelection)) {
            Stage stage= (Stage) sign_up.getScene().getWindow();
            Parent root= FXMLLoader.load(getClass().getResource("menu_admin.fxml"));
            Scene scene= new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    // Helper method to get the selected value from a ToggleGroup of RadioMenuItems
    private String getSelectedRadioButtonValue(ToggleGroup toggleGroup) {
        Toggle selectedToggle = toggleGroup.getSelectedToggle();
        if (selectedToggle != null && selectedToggle instanceof RadioMenuItem) {
            String text = ((RadioMenuItem) selectedToggle).getText();
            if (text.equals("masculin")) {
                return "m";
            } else if (text.equals("feminin")) {
                return "f";
            }else if(text.equals("user")){
                return "user";
            } else if(text.equals("admin")){
                return "admin";
            }
        }
        return null;
    }
}
