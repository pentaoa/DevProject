package edu.sustech.students.ura.devproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    void LoginBtnReleased() {
        String username = Input_name.getText();
        String passwd = Input_passwd.getText();
        if (username.equals("admin") && passwd.equals("admin")) {//判断用户名密码是否为某个值
            //弹出一个对话框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("登录成功");
            alert.setContentText("登录成功");
            alert.showAndWait();
        } else {
            //弹出一个对话框
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("提示");
            alert.setHeaderText("登录失败");
            alert.setContentText("登录失败");
            alert.showAndWait();
        }
    }
}
