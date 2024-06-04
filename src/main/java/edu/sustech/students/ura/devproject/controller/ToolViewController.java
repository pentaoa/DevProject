package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.util.AudioPlayer;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class ToolViewController {
    Word word = Word.getInstance();
    Random random = new Random();

    private double mouseX;
    private double mouseY;

    @FXML
    private ImageView cheems;

    @FXML
    private ImageView pop;

    @FXML
    private StackPane stack;

    @FXML
    private void initialize() {
        stack.getChildren().add(word);
        word.updateWord("你好！！！！");
        // 窗口位置更改器
        cheems.setOnMousePressed(event -> {
            // 记录鼠标按下时的位置
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        cheems.setOnMouseDragged(event -> {
            // 获取当前的舞台
            Stage stage = (Stage) cheems.getScene().getWindow();

            // 计算鼠标的移动距离
            double offsetX = event.getScreenX() - mouseX;
            double offsetY = event.getScreenY() - mouseY;

            // 移动窗口
            stage.setX(offsetX);
            stage.setY(offsetY);
        });

        // 创建一个TranslateTransition对象
        TranslateTransition transition = new TranslateTransition();

        // 设置动画的持续时间
        transition.setDuration(Duration.seconds(0.3));

        // 设置动画的起始和结束位置
        transition.setFromY(-10);
        transition.setToY(0);

        // 将动画应用到pop ImageView上
        transition.setNode(stack);

        cheems.setOnMouseClicked(event -> {
            AudioPlayer.playSound("src/main/resources/audio/dogsong.wav");
            // 开始动画
            transition.play();
            int randomWord = random.nextInt(2);
            switch (randomWord){
                case 0:
                    word.updateWord();
                    break;
                case 1:
                    word.updateWord2();
                    break;
            }

        });

    }
}
