package edu.sustech.students.ura.devproject.controller;

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
public class RegisterViewController {

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

    }
}
