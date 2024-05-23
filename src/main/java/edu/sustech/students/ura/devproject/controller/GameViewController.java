package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController {
    @FXML
    private Label stepLabel;
    private GameManager gameManager;
    private GameBoard gameBoard;
    private boolean GameHasWon = false;

    @FXML
    private AnchorPane gameViewCenter;

    @FXML
    private Button LoadButton;
    @FXML
    private Button MoveUp;
    @FXML
    private Button MoveDown;
    @FXML
    private Button MoveRight;
    @FXML
    private Button MoveLeft;

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

        RestartButton.setOnAction(event -> {
            ReStart();
            updateStepCount(0);
        });

        MoveUp.setOnAction(event -> { // 每次移动之后，检查胜利和失败
            gameManager.getGrid().moveUp();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("UP");
            handleMoveCompletion();
            handleLoseConditon();
        });
        MoveDown.setOnAction(event -> {
            gameManager.getGrid().moveDown();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("DOWN");
            handleMoveCompletion();
            handleLoseConditon();
        });
        MoveLeft.setOnAction(event -> {
            gameManager.getGrid().moveLeft();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("LEFT");
            handleMoveCompletion();
            handleLoseConditon();
        });
        MoveRight.setOnAction(event -> {
            gameManager.getGrid().moveRight();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("RIGHT");
            handleMoveCompletion();
            handleLoseConditon();
        });

        // 为按钮添加事件监听器
        Platform.runLater(() -> {
            gameViewCenter.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case W:
                        gameManager.getGrid().moveUp();
                        updateStepCount(gameManager.getGrid().getSteps());
                        System.out.println("UP");
                        handleMoveCompletion();
                        handleLoseConditon();
                        break;
                    case S:
                        gameManager.getGrid().moveDown();
                        updateStepCount(gameManager.getGrid().getSteps());
                        System.out.println("DOWN");
                        handleMoveCompletion();
                        handleLoseConditon();
                        break;
                    case A:
                        gameManager.getGrid().moveLeft();
                        updateStepCount(gameManager.getGrid().getSteps());
                        System.out.println("LEFT");
                        handleMoveCompletion();
                        handleLoseConditon();
                        break;
                    case D:
                        gameManager.getGrid().moveRight();
                        updateStepCount(gameManager.getGrid().getSteps());
                        System.out.println("RIGHT");
                        handleMoveCompletion();
                        handleLoseConditon();
                        break;
                }
            });
        });
    }

    // 初始化游戏
    private void InitialGame() {
        initializeStep();
        System.out.println("尝试启动游戏······");
        gameManager = new GameManager();
        gameBoard = new GameBoard(gameManager);
        GameHasWon = false;
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
    public void handleMoveCompletion() { //检测游戏是否胜利
        if(GameHasWon==false) {//这个if保证了只弹出一次胜利界面
            if (gameManager.getGrid().checkWin() >= 8) {
                showAlert("恭喜!", "你真是数学天才，达到了8分!");
                GameHasWon = true;
            }
        }
    }
    public void handleLoseConditon(){
        if(gameManager.getGrid().isGameOver()==true){
            showAlert("你输了", "你真是数学天才，你已经不能移动了");
        }
    }
    private void showAlert(String title, String content) { //用来弹出胜利界面
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addEventFilterToButton(Button button) {
        button.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    gameManager.getGrid().moveUp();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("UP");
                    handleMoveCompletion();
                    handleLoseConditon();
                    event.consume();
                    break;
                case DOWN:
                    gameManager.getGrid().moveDown();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("DOWN");
                    handleMoveCompletion();
                    handleLoseConditon();
                    event.consume();
                    break;
                case LEFT:
                    gameManager.getGrid().moveLeft();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("LEFT");
                    handleMoveCompletion();
                    handleLoseConditon();
                    event.consume();
                    break;
                case RIGHT:
                    gameManager.getGrid().moveRight();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("RIGHT");
                    handleMoveCompletion();
                    handleLoseConditon();
                    event.consume();
                    break;
                default:
                    break;
            }
        });
    }
    public void initializeStep() {
        updateStepCount(0);
    }
    public void ReStart(){
        gameManager.remake();
    }
    public void updateStepCount(int steps) {
        stepLabel.setText("步数: " + steps);
    }
}
