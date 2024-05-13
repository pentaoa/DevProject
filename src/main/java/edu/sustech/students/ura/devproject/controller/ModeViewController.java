package edu.sustech.students.ura.devproject.controller;

import javafx.fxml.FXML;

public class ModeViewController {
    @FXML
    void SrotyModeTrigger() {
        System.out.println("Story Mode");
    }
    @FXML
    void EasyModeTrigger() {
        System.out.println("Easy Mode");
    }
    @FXML
    void HardModeTrigger() {
        System.out.println("Hard Mode");
    }
}
