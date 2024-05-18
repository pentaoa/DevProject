package edu.sustech.students.ura.devproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane GridPane;

    @FXML
    private Button LoadButton;

    @FXML
    private Button PauseButton;

    @FXML
    private Button QuitButton;

    @FXML
    private Button RestartButton;

    @FXML
    private Button SaveButton;

    private void StartGame() {
        System.out.println("Game started");
    }

    private void PauseGame() {
        System.out.println("Game paused");
    }

    private void QuitGame() {
        System.out.println("Game quit");
    }


}
