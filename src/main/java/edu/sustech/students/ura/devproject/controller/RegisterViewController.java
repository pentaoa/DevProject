package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.*;
import edu.sustech.students.ura.devproject.model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * RegisterView
 * 注册界面
 * @version 1.0
 * 注册界面包括······
 */
public class RegisterViewController implements ClientListener{

    private Client client;

    @FXML
    private Button Button_Back;

    @FXML
    private Button Button_signup;

    @FXML
    private TextField Input_name;

    @FXML
    private PasswordField Input_passwd;

    @FXML
    private Label Text_Register;

    @FXML
    private Label text_signup;

    public RegisterViewController() {
        client = ClientManager.getClient();
        client.setListener(this);
    }

    @FXML
    void BackTrigger(ActionEvent event) {
        System.out.println("返回登录界面");
        try {
            // 加载游戏界面 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/login-view.fxml"));
            // 创建新的场景
            Scene scene = new Scene(loader.load());
            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Button_Back.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("登录 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常（例如，显示错误对话框）
        }
    }

    @FXML
    void SignupTrigger(ActionEvent event) {
        System.out.println("用户点击了注册按钮");
        String inputUsername = Input_name.getText();
        String inputPasswd = Input_passwd.getText();
        if (inputUsername.isEmpty() || inputPasswd.isEmpty()) {
            text_signup.setText("用户名或密码不能为空");
            return;
        }
        if (inputUsername.contains(" ") || inputPasswd.contains(" ")) {
            text_signup.setText("用户名或密码不能包含空格");
            return;
        }
        if (inputUsername.length() > 10 || inputPasswd.length() > 10) {
            text_signup.setText("用户名或密码长度不能超过20");
            return;
        }
        if (inputUsername.length() < 5 || inputPasswd.length() < 5) {
            text_signup.setText("用户名或密码长度不能小于5");
            return;
        }

        client.sendMessage("REGISTER:" + inputUsername + ":" + inputPasswd); //发送注册信息
    }

    @Override
    public void onMessageReceived(String message) {
        // 在 JavaFX 应用程序线程中更新 UI
        javafx.application.Platform.runLater(() -> {
            if (message.startsWith("REGISTER_SUCCESS")) {
                text_signup.setText("注册成功！");
                // Load next FXML
                loadNextFXML();
            } else if (message.startsWith("REGISTER_FAIL")) {
                text_signup.setText("注册失败：" + message.split(":")[1]);
            }
        });
    }

    public void loadNextFXML() {
        try {
            // Load login-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/login-view.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Get current stage and set scene
            Stage stage = (Stage) Button_signup.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("登录 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
