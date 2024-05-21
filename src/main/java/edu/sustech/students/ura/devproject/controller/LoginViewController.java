package edu.sustech.students.ura.devproject.controller;

import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * LoginViewController
 * 登录界面控制器
 * @version 1.0
 * 这是 2048 游戏的登录界面，用户在这里可以输入用户名和密码，然后点击登录按钮来登录游戏。
 */

public class LoginViewController {


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

    @FXML
    protected void LoginTrigger() {
        String inputUsername = Input_name.getText();
        String inputPasswd = Input_passwd.getText();
        if (inputUsername.equals("admin") && inputPasswd.equals("admin")) {
            // Login success
            try {
                // Load mode-view.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/mode-view.fxml"));
                Scene modeScene = new Scene(loader.load());

                // Get current stage and set scene
                Stage stage = (Stage) Button_login.getScene().getWindow();
                stage.setScene(modeScene);
                stage.setTitle("模式选择 | 2048");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception (e.g., show an error dialog)
            }
        } else {
            // Login failure
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("登录失败");
            alert.showAndWait();
        }
    }
    @FXML
    protected void OfflineTrigger() {
        System.out.println("用户进入离线模式。");
        try {
            // 直接加载游戏界面 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            Scene offLineScene = new Scene(loader.load());
            offLineScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/game.css")).toExternalForm());

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
}
