package edu.sustech.students.ura.devproject.controller;

import animatefx.animation.Bounce;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;

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
        new Pulse(Text_title).play();
        //弹出一个对话框
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText("离线模式");
        alert.setContentText("离线模式");
        alert.showAndWait();
    }
    @FXML
    protected void RegisterTrigger() {
        try {
            // Load register-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/register-view.fxml"));
            Scene registerScene = new Scene(loader.load());

            // Get current stage and set scene
            Stage stage = (Stage) Button_register.getScene().getWindow();
            stage.setScene(registerScene);
            stage.setTitle("注册 | 2048");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show an error dialog)
        }
    }
}
