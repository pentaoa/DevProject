package edu.sustech.students.ura.devproject;

import edu.sustech.students.ura.devproject.client.*;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GameLauncher extends Application {
    ClientManager clientManager;
    Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));

    @Override
    public void start(Stage stage) throws IOException {
        // 设置主舞台的关闭请求事件
        stage.setOnCloseRequest(event -> {
            AudioPlayer.playSound("src/main/resources/audio/shutdown.wav");
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 关闭整个JavaFX应用程序
            Platform.exit();
        });
//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(icon);
        // 播放开始音乐
        AudioPlayer.playStaticSound("src/main/resources/audio/start.wav");
        // 加载视频
        File videoFile = new File("src/main/resources/video/open.mp4");
        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        stage.setMinHeight(460);
        stage.setMinWidth(650);
        stage.setMaxHeight(600);
        stage.setMaxWidth(900);
        // 设置视频播放器的大小
//        mediaView.setFitWidth(630);
        mediaView.setFitHeight(440);
        mediaPlayer.setAutoPlay(true); // 设置视频自动播放
        // 添加鼠标点击事件, 点击鼠标时停止播放动画
        mediaView.setOnMouseClicked(event -> {
            mediaPlayer.stop(); // 停止播放动画
            try {
                showLoginView(stage); // 加载主菜单
                showToolStage(stage); // 加载工具栏
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

    public void showToolStage(Stage stage) throws IOException {
        // 新建一个舞台
        Stage toolStage = new Stage();
        toolStage.getIcons().add(icon);
        toolStage.initStyle(StageStyle.TRANSPARENT);

        // 加载工具界面
        FXMLLoader fxmlLoader = new FXMLLoader(GameLauncher.class.getResource("tool-view.fxml"));
        Scene toolScene = new Scene(fxmlLoader.load());
        toolScene.setFill(Color.TRANSPARENT);

        toolStage.setScene(toolScene);
        toolStage.setTitle("工具 | 2048");

        // 设置工具界面的位置
        toolStage.setX(stage.getX()-toolStage.getWidth());
        toolStage.setY(stage.getY());
        toolStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}