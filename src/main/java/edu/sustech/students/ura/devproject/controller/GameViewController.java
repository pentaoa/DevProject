package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * GameController
 * 游戏界面控制器
 * @version 1.0
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 *
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 */
public class GameViewController {

//    @FXML
//    private GridPane GridPane;
    @FXML
    private Button MoveUp;
    @FXML
    private Button MoveDown;
    @FXML
    private Button MoveLeft;
    @FXML
    private Button MoveRight;

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

    private GameBoard gameBoard;

    // 初始化游戏
    private void InitialGame() {
        System.out.println("尝试启动游戏······");
        GameManager gameManager = new GameManager();
        gameBoard = new GameBoard();

        //移除原来的GameBoard存在，则移除

        // 将新的GameBoard添加到GridPane中
    }

    private void PauseGame() {
        // 暂停游戏
        System.out.println("Game paused");
    }

    private void QuitGame() {
        // 退出游戏
        System.out.println("Game quit");
    }

}
