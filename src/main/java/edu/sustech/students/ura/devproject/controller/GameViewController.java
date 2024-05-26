package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.GridNumber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.Random;


/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController {
    private Random random = new Random();
    @FXML
    private Label timeLabel; // 假设这是用于显示时间的Label
    @FXML
    private Label gradeLabel;
    private Timeline timer; // 用于计时的Timeline
    private int remainingTime = 10; // 初始剩余时间
    private boolean EasyMode = false;
    private boolean TimeMode = false;
    private boolean isGameActive = true; //这个状态用于实现时间到后，不能再移动
    @FXML
    private Label stepLabel;
    private GameManager gameManager;
    private GameBoard gameBoard;
    private boolean GameHasWon = false;

    @FXML
    private AnchorPane gameViewCenter;
    @FXML
    private Button AIButton;
    @FXML
    private Button ReviveButton;
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
            if(EasyMode==false)
            {ReStart();}
            else {
                ReStart();
                EasyMode();
            }
            updateStepCount(0);
        });

        ReviveButton.setOnAction(event -> {
            gameManager.grid.Revise();
        });

        AIButton.setOnAction(event -> {
            AutoChoosing();
            handleMoveCompletion();
            handleLoseCondition();
            updateScoreDisplay(gameManager.grid.getGrades());
            updateStepCount(gameManager.grid.getSteps());
        });

        MoveUp.setOnAction(event -> { // 每次移动之后，检查胜利和失败
            if(isGameActive==true){
            gameManager.getGrid().moveUp();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("UP");
            handleMoveCompletion();
            handleLoseCondition();
            updateScoreDisplay(gameManager.grid.getGrades());
            }
            else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("游戏结束");
                    alert.setHeaderText(null);
                    alert.setContentText("游戏已结束，你不能再移动了！");
                    alert.showAndWait();
                });
            }
        });
        MoveDown.setOnAction(event -> {
            if(isGameActive==true){
            gameManager.getGrid().moveDown();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("DOWN");
            handleMoveCompletion();
            handleLoseCondition();
            updateScoreDisplay(gameManager.grid.getGrades());
            }
            else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("游戏结束");
                    alert.setHeaderText(null);
                    alert.setContentText("游戏已结束，你不能再移动了！");
                    alert.showAndWait();
                });
            }
        });
        MoveLeft.setOnAction(event -> {
            if(isGameActive==true){
            gameManager.getGrid().moveLeft();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("LEFT");
            handleMoveCompletion();
            handleLoseCondition();
            updateScoreDisplay(gameManager.grid.getGrades());
            }
            else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("游戏结束");
                    alert.setHeaderText(null);
                    alert.setContentText("游戏已结束，你不能再移动了！");
                    alert.showAndWait();
                });
            }
        });
        MoveRight.setOnAction(event -> {
            if(isGameActive==true){
            gameManager.getGrid().moveRight();
            updateStepCount(gameManager.getGrid().getSteps());
            System.out.println("RIGHT");
            handleMoveCompletion();
            handleLoseCondition();
            updateScoreDisplay(gameManager.grid.getGrades());
            }
            else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("游戏结束");
                    alert.setHeaderText(null);
                    alert.setContentText("游戏已结束，你不能再移动了！");
                    alert.showAndWait();
                });
            }
        });

        // 为按钮添加事件监听器
        Platform.runLater(() -> {
            gameViewCenter.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case W:
                        if(isGameActive==true){
                        gameManager.getGrid().moveUp();
                        updateStepCount(gameManager.getGrid().getSteps());
                        System.out.println("UP");
                        handleMoveCompletion();
                        handleLoseCondition();
                        updateScoreDisplay(gameManager.grid.getGrades());
                        break;
                        }
                        else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("游戏结束");
                                alert.setHeaderText(null);
                                alert.setContentText("游戏已结束，你不能再移动了！");
                                alert.showAndWait();
                            });
                        }
                    case S:
                        if(isGameActive==true) {
                            gameManager.getGrid().moveDown();
                            updateStepCount(gameManager.getGrid().getSteps());
                            System.out.println("DOWN");
                            handleMoveCompletion();
                            handleLoseCondition();
                            updateScoreDisplay(gameManager.grid.getGrades());
                            break;
                        }
                        else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("游戏结束");
                                alert.setHeaderText(null);
                                alert.setContentText("游戏已结束，你不能再移动了！");
                                alert.showAndWait();
                            });
                        }
                    case A:
                        if(isGameActive==true) {
                            gameManager.getGrid().moveLeft();
                            updateStepCount(gameManager.getGrid().getSteps());
                            System.out.println("LEFT");
                            handleMoveCompletion();
                            handleLoseCondition();
                            updateScoreDisplay(gameManager.grid.getGrades());
                            break;
                        }
                        else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("游戏结束");
                                alert.setHeaderText(null);
                                alert.setContentText("游戏已结束，你不能再移动了！");
                                alert.showAndWait();
                            });
                        }
                    case D:
                        if(isGameActive==true) {
                            gameManager.getGrid().moveRight();
                            updateStepCount(gameManager.getGrid().getSteps());
                            System.out.println("RIGHT");
                            handleMoveCompletion();
                            handleLoseCondition();
                            updateScoreDisplay(gameManager.grid.getGrades());
                            break;
                        }
                        else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("游戏结束");
                                alert.setHeaderText(null);
                                alert.setContentText("游戏已结束，你不能再移动了！");
                                alert.showAndWait();
                            });
                        }
                }
            });
        });
    }

    // 初始化游戏
    private void InitialGame() {
        initializeStep();
        System.out.println("尝试启动游戏······");
        gameManager = new GameManager(1);
        gameBoard = new GameBoard(gameManager);
        GameHasWon = false;
        EasyMode = false;
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
    public void handleLoseCondition(){
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
                    if(isGameActive==true){
                    gameManager.getGrid().moveUp();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("UP");
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    updateScoreDisplay(gameManager.grid.getGrades());
                    break;
                    }
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("游戏结束");
                            alert.setHeaderText(null);
                            alert.setContentText("游戏已结束，你不能再移动了！");
                            alert.showAndWait();
                        });
                    }
                case DOWN:
                    if(isGameActive==true){
                    gameManager.getGrid().moveDown();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("DOWN");
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    updateScoreDisplay(gameManager.grid.getGrades());
                    break;
                    }
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("游戏结束");
                            alert.setHeaderText(null);
                            alert.setContentText("游戏已结束，你不能再移动了！");
                            alert.showAndWait();
                        });
                    }
                case LEFT:
                    if(isGameActive==true){
                    gameManager.getGrid().moveLeft();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("LEFT");
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    updateScoreDisplay(gameManager.grid.getGrades());
                    break;
                    }
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("游戏结束");
                            alert.setHeaderText(null);
                            alert.setContentText("游戏已结束，你不能再移动了！");
                            alert.showAndWait();
                        });
                    }
                case RIGHT:
                    if(isGameActive==true){
                    gameManager.getGrid().moveRight();
                    updateStepCount(gameManager.getGrid().getSteps());
                    System.out.println("RIGHT");
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    updateScoreDisplay(gameManager.grid.getGrades());
                    break;
                    }
                    else {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("游戏结束");
                            alert.setHeaderText(null);
                            alert.setContentText("游戏已结束，你不能再移动了！");
                            alert.showAndWait();
                        });
                    }
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
        if(TimeMode==true){//当其是计时模式时，才采用计时的重开
        isGameActive = true;
            if (timer != null) {
            timer.stop(); // 停止当前计时器
            timer = null; // 帮助垃圾回收
        }
        remainingTime = 10; // 重置剩余时间到初始值
        updateTimerDisplay(remainingTime); // 更新UI显示新的剩余时间
        if(TimeMode == true)
            TimeMode();
        }
    }
    public void updateStepCount(int steps) {
        stepLabel.setText("步数: " + steps);
    }
    public void EasyMode(){
        gameManager.EasyMode();
        EasyMode=true;
    }
    public void TimeMode() {
        TimeMode = true;
        // 开始计时模式
        if (timer == null) {
            timer = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        remainingTime--;
                        updateTimerDisplay(remainingTime);
                        if (remainingTime <= 0) {
                            handleTimeExpired();
                        }
                    })
            );
            timer.setCycleCount(Animation.INDEFINITE); // 循环播放
            timer.play();
        }
    }
    private void updateTimerDisplay(int timeLeft) {
        Platform.runLater(() -> {
            timeLabel.setText("时间: " + timeLeft);
        });

    }
    private void handleTimeExpired() {
        if (timer != null) { // 确保只停止一次
            timer.stop();
            timer = null; // 释放引用
        }
        Platform.runLater(() -> {
            timeLabel.setText("时间耗尽！");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("游戏结束");
            alert.setHeaderText(null);
            alert.setContentText("时间已到，游戏结束！");
            alert.showAndWait();
            isGameActive = false;
            // 游戏结束处理逻辑...
        });
    }
    public void updateScoreDisplay(int score) {
        Platform.runLater(() -> {
            gradeLabel.setText("分数: " + score);
        });
    }
    public void AutoChoosing (){
        int maxScore = -1;
        int bestDirection = -1; // 尝试所有可能的移动方向
        for (int direction = 0; direction < 4; direction++) {
            GridNumber tempGrid = gameManager.grid.clone(); // 使用clone方法来避免更改原始grid
            int scoreBeforeMove = tempGrid.getGrades(); // 记录移动前的分数
            // 根据方向执行移动
            switch (direction) {
                case 0: tempGrid.moveUp(); break;
                case 1: tempGrid.moveDown(); break;
                case 2: tempGrid.moveLeft(); break;
                case 3: tempGrid.moveRight(); break;
            }
            int scoreAfterMove = tempGrid.getGrades(); // 计算这次移动后的总分数
            // 检查是否有得分增加，即移动有效
            if (scoreAfterMove > scoreBeforeMove && scoreAfterMove > maxScore) {
                maxScore = scoreAfterMove;
                bestDirection = direction;
            }
        }
// 如果所有方向都没有得分增加，随机选择一个方向
        if (maxScore <= gameManager.grid.getGrades()) {
            bestDirection = random.nextInt(4); // 随机选择0-3，代表上、下、左、右
        }
// 根据最佳方向执行实际的移动操作
        switch (bestDirection) {
            case 0: gameManager.grid.moveUp();
            break;
            case 1: gameManager.grid.moveDown();
            break;
            case 2: gameManager.grid.moveLeft();
            break;
            case 3: gameManager.grid.moveRight();
            break;
        }
    }
}

