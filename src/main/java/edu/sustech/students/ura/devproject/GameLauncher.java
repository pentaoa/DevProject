package edu.sustech.students.ura.devproject;

import edu.sustech.students.ura.devproject.client.*;
import edu.sustech.students.ura.devproject.controller.LoginViewController;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GameLauncher extends Application {
    ClientManager clientManager;

    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        stage.getIcons().add(icon);
        // 播放开始音乐
        AudioPlayer.playStaticSound("src/main/resources/audio/start.wav");
        // 加载视频
        File videoFile = new File("src/main/resources/video/open.mp4");
        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        stage.setHeight(440);
        stage.setWidth(620);
        stage.setMinHeight(440);
        stage.setMinWidth(620);
        stage.setMaxHeight(600);
        stage.setMaxWidth(900);
        // 设置视频播放器的大小
        mediaView.setFitWidth(620);
        mediaView.setFitHeight(410);
        mediaPlayer.setAutoPlay(true); // 设置视频自动播放
        // 添加鼠标点击事件, 点击鼠标时停止播放动画
        mediaView.setOnMouseClicked(event -> {
            mediaPlayer.stop(); // 停止播放动画
            try {
                showLoginView(stage); // 加载主菜单
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 设置视频播放结束后的事件
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                showLoginView(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Group anime = new Group(mediaView);
        Scene scene = new Scene(anime);
        stage.setScene(scene);
        stage.setTitle("2048");
        stage.show();
        Client client = ClientManager.getClient();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    private void showLoginView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameLauncher.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("登录 | 2048");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}