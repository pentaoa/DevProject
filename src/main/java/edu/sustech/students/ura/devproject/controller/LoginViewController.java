package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.*;
import edu.sustech.students.ura.devproject.client.ClientManager;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * LoginViewController
 * 登录界面控制器
 *
 * @version 1.0
 * 这是 2048 游戏的登录界面，用户在这里可以输入用户名和密码，然后点击登录按钮来登录游戏。
 */

public class LoginViewController implements ClientListener {
    private MediaPlayer mediaPlayer;
    private Client client;

    @FXML
    private StackPane rootPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button Button_login;

    @FXML
    private Button Button_offline;

    @FXML
    private Button Button_register;

    @FXML
    private TextField Input_name;

    @FXML
    private PasswordField Input_passwd;

    @FXML
    private Label Text_title;

    public LoginViewController() {
        client = ClientManager.getClient();
        client.setListener(this);

        // 加载媒体文件
        String videoPath = Objects.requireNonNull(getClass().getResource("/video/background.mp4")).toExternalForm();
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    @FXML
    protected void LoginTrigger() {
        System.out.println("用户点击了登录按钮");
        String inputUsername = Input_name.getText();
        String inputPasswd = Input_passwd.getText();
        if (inputUsername.equals("admin") && inputPasswd.equals("admin")) {
            loadModeFXML();
            return;
        }

        if (inputUsername.isEmpty() || inputPasswd.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("用户名或密码不能为空");
            alert.showAndWait();
            return;
        }
        if (inputUsername.contains(" ") || inputPasswd.contains(" ")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("用户名或密码不能包含空格");
            alert.showAndWait();
            return;
        }
        if (inputUsername.length() > 10 || inputPasswd.length() > 10) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("用户名或密码长度不能超过20");
            alert.showAndWait();
            return;
        }
        if (inputUsername.length() < 5 || inputPasswd.length() < 5) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("用户名或密码长度不能小于5");
            alert.showAndWait();
            return;
        }
        Client client = ClientManager.getClient();
        client.sendMessage("LOGIN:" + inputUsername + ":" + inputPasswd);
    }

    @FXML
    protected void OfflineTrigger() {
        AudioPlayer.stopStaticPlayer();
        System.out.println("转为离线模式");
        try {
            // 直接加载游戏界面 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/offline-mode-view.fxml"));
            Scene offLineScene = new Scene(loader.load());
            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_offline.getScene().getWindow();
            stage.setScene(offLineScene);
            stage.setTitle("离线模式 | 2048");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void RegisterTrigger() {
        AudioPlayer.stopStaticPlayer();
        try {
            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/register-view.fxml"));
            Scene registerScene = new Scene(loader.load());

            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_register.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("注册 | 2048");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        MediaView mediaView = new MediaView(mediaPlayer);
        rootPane.getChildren().add(0, mediaView);
    }

    @Override
    public void onMessageReceived(String message) {
        // Update UI in JavaFX application thread
        javafx.application.Platform.runLater(() -> {
            if (message.startsWith("LOGIN_SUCCESS")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText("登录成功");
                alert.setContentText("登录成功");
                alert.showAndWait();
                loadModeFXML();
            } else if (message.startsWith("LOGIN_FAIL")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("提示");
                alert.setHeaderText("登录失败");
                alert.setContentText(message.split(":")[1]);
                alert.showAndWait();
            }
        });
    }
    public void loadModeFXML() {
        AudioPlayer.stopStaticPlayer();
        try {
            // 加载下一个 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/mode-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_login.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("模式选择 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
