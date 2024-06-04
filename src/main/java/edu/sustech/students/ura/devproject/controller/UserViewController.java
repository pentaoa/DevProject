package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.Client;
import edu.sustech.students.ura.devproject.client.ClientListener;
import edu.sustech.students.ura.devproject.client.ClientManager;
import edu.sustech.students.ura.devproject.model.GameStatus;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserViewController implements ClientListener {
    @FXML
    private Label Text_HelloUser;

    @FXML
    private Button button_newGame;

    @FXML
    private Button button_readGame;

    @FXML
    private Button button_setPassword;

    @FXML
    private Label text_boardClassical;

    @FXML
    private Label text_boardObstacle;

    @FXML
    private Label text_boardTime;

    @FXML
    private Label text_gradeBoard;

    @FXML
    private Label text_scores;

    @FXML
    private Label text_scoresClassical;

    @FXML
    private Label text_scoresObstacle;

    @FXML
    private Label text_scoresTime;

    GameStatus status = GameStatus.getInstance();
    private Client client;


    @FXML
    void initialize() throws InterruptedException {
        client = ClientManager.getClient();
        client.setListener(this);
        Text_HelloUser.setText("你好，" + status.getUsername());
        client.sendMessage("GET_HIGHSCORE:" + status.getUsername()+":"+1);
        Thread.sleep(30);
        client.sendMessage("GET_HIGHSCORE:" + status.getUsername()+":"+2);
        Thread.sleep(30);
        client.sendMessage("GET_HIGHSCORE:" + status.getUsername()+":"+3);
        Thread.sleep(30);
        client.sendMessage("GET_GRADE:" + status.getUsername()+":"+1);
        Thread.sleep(30);
        client.sendMessage("GET_GRADE:" + status.getUsername()+":"+2);
        Thread.sleep(30);
        client.sendMessage("GET_GRADE:" + status.getUsername()+":"+3);
    }

    @FXML
    void newGame(ActionEvent event) {
        try {
            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/mode-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // 获取当前的舞台并设置场景
            Stage stage = (Stage) button_newGame.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("模式选择 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(String message) {
        // Update UI in JavaFX application thread
        javafx.application.Platform.runLater(() -> {
            if (message.startsWith("GET_HIGHSCORE")) {
                String[] parts = message.split(":");
                int mode = Integer.parseInt(parts[1]);
                int highScore = Integer.parseInt(parts[2]);
                switch (mode) {
                    case 1:
                        text_scoresClassical.setText("经典高分：" + highScore);
                        break;
                    case 2:
                        text_scoresObstacle.setText("障碍高分：" + highScore);
                        break;
                    case 3:
                        text_scoresTime.setText("限时高分：" + highScore);
                        break;
                }
            } else if (message.startsWith("GET_GRADE")) {
                String[] parts = message.split(":");
                int mode = Integer.parseInt(parts[1]);
                int grade = Integer.parseInt(parts[2]);
                switch (mode) {
                    case 1:
                        text_boardClassical.setText("经典等级：" + grade);
                        break;
                    case 2:
                        text_boardObstacle.setText("障碍等级：" + grade);
                        break;
                    case 3:
                        text_boardTime.setText("限时等级：" + grade);
                        break;
                }
            }
        });
    }
}
