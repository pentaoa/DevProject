package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController {
    private GameManager gameManager;
    private GameBoard gameBoard;

    @FXML
    private AnchorPane gameViewCenter;

    @FXML
    private Button LoadButton;

    @FXML
    private Button PauseButton;

    @FXML
    private Button QuitButton;

    @FXML
    private Button RestartButton;

    @FXML
    private Button SaveButton;

    @FXML
    private void initialize() {
        InitialGame();

        // 为按钮添加事件过滤器
        addEventFilterToButton(LoadButton);
        addEventFilterToButton(PauseButton);
        addEventFilterToButton(QuitButton);
        addEventFilterToButton(RestartButton);
        addEventFilterToButton(SaveButton);

        // 为按钮添加事件监听器
        Platform.runLater(() -> {
            gameViewCenter.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case W:
                        gameManager.getGrid().moveUp();
                        System.out.println("UP");
                        break;
                    case S:
                        gameManager.getGrid().moveDown();
                        System.out.println("DOWN");
                        break;
                    case A:
                        gameManager.getGrid().moveLeft();
                        System.out.println("LEFT");
                        break;
                    case D:
                        gameManager.getGrid().moveRight();
                        System.out.println("RIGHT");
                        break;
                }
            });
        });
    }

    // 初始化游戏
    private void InitialGame() {
        System.out.println("尝试启动游戏······");
        gameManager = new GameManager();
        gameBoard = new GameBoard(gameManager);

        //移除原来的GameBoard存在，则移除
        //将新的GameBoard显示在视图中心
        gameViewCenter.getChildren().add(gameBoard);

    }

    private void PauseGame() {
        // 暂停游戏
        System.out.println("Game paused");
    }

    private void QuitGame() {
        // 退出游戏
        System.out.println("Game quit");
    }

    private void addEventFilterToButton(Button button) {
        button.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    gameManager.getGrid().moveUp();
                    System.out.println("UP");
                    event.consume();
                    break;
                case DOWN:
                    gameManager.getGrid().moveDown();
                    System.out.println("DOWN");
                    event.consume();
                    break;
                case LEFT:
                    gameManager.getGrid().moveLeft();
                    System.out.println("LEFT");
                    event.consume();
                    break;
                case RIGHT:
                    gameManager.getGrid().moveRight();
                    System.out.println("RIGHT");
                    event.consume();
                    break;
                default:
                    break;
            }
        });
    }
}
