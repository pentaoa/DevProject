package edu.sustech.students.ura.devproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ModeViewController {

    @FXML
    private Button Button_classical;

    @FXML
    private Button Button_hard;

    @FXML
    private Button Button_obstacle;

    @FXML
    private Label Text_HelloUser;

    @FXML
    void EasyModeTrigger(ActionEvent event) {
        loadGameFXML();
    }

    @FXML
    void HardModeTrigger(ActionEvent event) {

    }

    @FXML
    void StoryModeTrigger(ActionEvent event) {

    }

    public void loadGameFXML() {
        try {
            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_classical.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("某某模式 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
