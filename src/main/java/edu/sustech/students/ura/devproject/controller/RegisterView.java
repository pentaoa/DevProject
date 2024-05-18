package edu.sustech.students.ura.devproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RegisterView {

    @FXML
    private Button Button_Back;

    @FXML
    private Label Text_Register;

    @FXML
    void BackTrigger(ActionEvent event) {
        System.out.println("返回登录界面");
    }

}
