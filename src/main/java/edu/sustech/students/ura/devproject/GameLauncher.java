package edu.sustech.students.ura.devproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setMinHeight(440);
        stage.setMinWidth(620);
        showLoginView(stage);

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