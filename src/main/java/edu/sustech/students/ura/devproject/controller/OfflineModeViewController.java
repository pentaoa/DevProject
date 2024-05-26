package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class OfflineModeViewController {

    @FXML
    private Button Button_classical;

    @FXML
    private Button Button_time;

    @FXML
    private Button Button_obstacle;

    @FXML
    private Label Text_HelloUser;

    @FXML
    void EasyModeTrigger(ActionEvent event) {
        try {
            GameStatus status = GameStatus.getInstance();
            status.setMode(1);

            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_classical.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("经典模式 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ObstacleModeTrigger() {
        try {
            GameStatus status = GameStatus.getInstance();
            status.setMode(2);

            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene obstacleScene = new Scene(loader.load());

            // 获取当前的舞台并设置新的场景
            Stage stage = (Stage) Button_obstacle.getScene().getWindow();
            stage.setScene(obstacleScene);
            stage.setTitle("障碍模式 | 2048"); // 修改标题以反映当前模式
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void TimeModeTrigger(ActionEvent event) {
        try {
            GameStatus status = GameStatus.getInstance();
            status.setMode(3);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene timeScene = new Scene(loader.load());

            // 获取当前的舞台并设置新的场景
            Stage stage = (Stage) Button_time.getScene().getWindow();
            stage.setScene(timeScene);
            stage.setTitle("计时模式 | 2048"); // 修改标题以反映当前模式
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
