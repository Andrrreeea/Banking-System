package com.example.banking_system_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu_Admin_Controller {

    @FXML
    private Button back;

    @FXML
    private TitledPane balance;

    @FXML
    private Button balance_users;

    @FXML
    private Button deposit;

    @FXML
    private Button withdraw;

    @FXML
    void choose_balance_users(ActionEvent event) throws IOException {
        Stage stage= (Stage) balance_users.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("log in.fxml"));
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void choose_deposit(ActionEvent event) throws IOException{
        Stage stage= (Stage) deposit.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("deposit.fxml"));
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void choose_withdraw(ActionEvent event) throws IOException {
        Stage stage= (Stage) withdraw.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("withdraw.fxml"));
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void go_back(ActionEvent event) throws IOException {
        Stage stage= (Stage) back.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("log in.fxml"));
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
