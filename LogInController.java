package com.example.banking_system_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private Button LOGIN;

    @FXML
    private Button SIGNUP;

    @FXML
    void handleLoginButton(ActionEvent event) throws IOException {
        Stage stage= (Stage) LOGIN.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("log_in.fxml"));
        Scene  scene= new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void handleSignupButton(ActionEvent event) throws IOException {
        Stage stage= (Stage) SIGNUP.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("sign_up.fxml"));
        Scene  scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
