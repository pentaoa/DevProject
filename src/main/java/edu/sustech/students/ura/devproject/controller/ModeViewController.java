package edu.sustech.students.ura.devproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * ModeViewController
 * 模式选择界面控制器
 * @version 1.0
 * 这是 2048 游戏的模式选择界面，用户在这里可以选择游戏的模式，例如简单模式、困难模式等。
 */

public class ModeViewController {
    @FXML
    private Label Text_HelloUser;

    @FXML
    protected void StoryModeTrigger() {
        System.out.println("Story Mode");
    }
    @FXML
    protected void EasyModeTrigger() {
        // 将显示游戏主界面
        try {
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/game-view.fxml"));
            // 创建新的场景
            Scene scene = new Scene(loader.load());
            // 获取当前的舞台并设置场景
            Stage stage = (Stage) Text_HelloUser.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("简单模式 | 2048");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常（例如，显示错误对话框）
        }
        System.out.println("用户进入简单模式。");
    }
    @FXML
    protected void HardModeTrigger() {
        System.out.println("Hard Mode");
    }
    public void updateHelloUserText(String username) {
        Text_HelloUser.setText("欢迎，" + username);
    }
}
