package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameStatus;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ModeViewController {
    GameStatus status = GameStatus.getInstance();

    @FXML
    private Button Button_Cheating;

    @FXML
    private Button Button_Happy;

    @FXML
    private Button Button_classical;

    @FXML
    private Button Button_obstacle;

    @FXML
    private Button Button_time;

    @FXML
    private Label Text_HelloUser;

    @FXML
    private Button button_Set;

    @FXML
    private Button button_StartGame;

    @FXML
    private Button button_theme;

    @FXML
    private CheckBox check_music;

    @FXML
    private CheckBox check_sound;

    @FXML
    private TextField numberInputer;

    @FXML
    private TextField scoreInputer;

    @FXML
    private Label text_ModeSet;

    @FXML
    private Label text_TargetSet;

    @FXML
    private Label text_settings;

    @FXML
    private Label text_status;


    @FXML
    void initialize() {
        Text_HelloUser.setText("你好，" + status.getUsername());
    }

    @FXML
    void SetTarget(ActionEvent event) {
        try {
            if(numberInputer.getText()!=null) {
                int targetNumber = Integer.parseInt(numberInputer.getText());
                status.setTargetNumber(targetNumber);
            }
            if(scoreInputer.getText()!=null) {
                int targetScore = Integer.parseInt(scoreInputer.getText());
                status.setTargetScore(targetScore);
            }
            ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
            pulse.setFromX(1);
            pulse.setFromY(1);
            pulse.setToX(1.1);
            pulse.setToY(1.1);
            pulse.setCycleCount(2);
            pulse.setAutoReverse(true);
            pulse.playFromStart();

            text_status.setText("目标数字 " + status.getTargetNumber()+";目标分数 " + status.getTargetScore());

            pulse.playFromStart();
        } catch (Exception e) {
            TranslateTransition shake = new TranslateTransition(Duration.seconds(0.1), text_status);
            shake.setFromX(0);
            shake.setByX(10);
            shake.setCycleCount(2);
            shake.setAutoReverse(true);
            shake.playFromStart();

            text_status.setText("请输入一个有效的数字！");
        }
    }

    @FXML
    void EasyModeTrigger(ActionEvent event) {
        status.setMode(1);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.playFromStart();
        text_status.setText("已设置为经典模式");
        pulse.playFromStart();
    }

    @FXML
    void ObstacleModeTrigger() {
        status.setMode(2);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.playFromStart();
        text_status.setText("已设置为障碍模式");
        pulse.playFromStart();
    }

    @FXML
    void TimeModeTrigger(ActionEvent event) {
        status.setMode(3);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.playFromStart();
        text_status.setText("已设置为计时模式");
        pulse.playFromStart();
    }
    @FXML
    void CheatingModeTrigger(ActionEvent event) {
        status.setMode(4);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.playFromStart();
        text_status.setText("已设置为作弊模式");
        pulse.playFromStart();
    }
    @FXML
    void HappyModeTrigger(ActionEvent event) {
        status.setMode(5);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.2), text_status);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.playFromStart();
        text_status.setText("已设置为欢乐模式");
        pulse.playFromStart();
    }
    @FXML
    void StartGame(ActionEvent event) {
        status.isSoundOn = check_sound.isSelected();
        status.isMusicOn = check_music.isSelected();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene timeScene = new Scene(loader.load());

            // 获取当前的舞台并设置新的场景
            Stage stage = (Stage) button_StartGame.getScene().getWindow();
            stage.setScene(timeScene);
            stage.setTitle("在线模式 | 2048"); // 修改标题以反映当前模式
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void SetTheme(ActionEvent event) {
        status.theme ++;
        if (status.theme > 3) {
            status.theme = 0;
        }
        if (status.theme == 0) {
            text_status.setText("主题：摩卡");
        } else if (status.theme == 1) {
            text_status.setText("主题：小荷");
        } else if (status.theme == 2) {
            text_status.setText("主题：一股OG味");
        } else if (status.theme == 3) {
            text_status.setText("主题：枫桥夜泊");
        }
    }
}