package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.Client;
import edu.sustech.students.ura.devproject.client.ClientManager;
import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.GameStatus;
import edu.sustech.students.ura.devproject.model.LocalSaveManager;
import edu.sustech.students.ura.devproject.util.Direction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Objects;

/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController {
    private int mode;
    private GameStatus status = GameStatus.getInstance();
    private GameManager gameManager;
    private GameBoard gameBoard;
    private boolean GameHasWon = false;
    private Client client = ClientManager.getClient();
    private LocalSaveManager localSaveManager = new LocalSaveManager();
//    private Timeline timeline;

    @FXML
    private Button LoadButton;

    @FXML
    private Button MoveDown;

    @FXML
    private Button MoveLeft;

    @FXML
    private Button MoveRight;

    @FXML
    private Button MoveUp;

    @FXML
    private Button PauseButton;

    @FXML
    private Button QuitButton;

    @FXML
    private Button RestartButton;

    @FXML
    private Button SaveButton;

    @FXML
    private AnchorPane gameViewCenter;

    @FXML
    private Label text_ScoreNumber;

    @FXML
    private Label text_TimeNumber;

    @FXML
    private Label text_score;

    @FXML
    private Label text_step;

    @FXML
    private Label text_time;

    @FXML
    private void initialize() {

        initialGame();

        // 为按钮添加事件过滤器
        addEventFilterToButton(LoadButton);
        addEventFilterToButton(PauseButton);
        addEventFilterToButton(QuitButton);
        addEventFilterToButton(RestartButton);
        addEventFilterToButton(SaveButton);
        addEventFilterToButton(MoveUp);
        addEventFilterToButton(MoveDown);
        addEventFilterToButton(MoveLeft);
        addEventFilterToButton(MoveRight);

        SaveButton.setOnAction(event -> {
            localSaveManager.save(gameManager);
        });

        LoadButton.setOnAction(event -> {
            gameManager = localSaveManager.load();
            gameViewCenter.getChildren().remove(gameBoard);
            gameBoard = new GameBoard(gameManager);
            gameViewCenter.getChildren().add(gameBoard);
        });

        RestartButton.setOnAction(event -> {
            ReStart();
            updateStepCount(0);

        });

        PauseButton.setOnAction(event -> {
            pauseSwitch();
        });

        QuitButton.setOnAction(event -> {
            // TODO: 退出游戏，返回用户登录后的界面，或者模式选择界面
        });

        MoveUp.setOnAction(event -> { // 每次移动之后，检查胜利和失败
            gameManager.moveUp();
            updateStepCount(gameManager.getSteps());
            updateScore(gameManager.getScore());
            handleMoveCompletion();
            handleLoseCondition();
        });
        MoveDown.setOnAction(event -> {
            gameManager.moveDown();
            updateStepCount(gameManager.getSteps());
            updateScore(gameManager.getScore());
            handleMoveCompletion();
            handleLoseCondition();
        });
        MoveLeft.setOnAction(event -> {
            gameManager.moveLeft();
            updateStepCount(gameManager.getSteps());
            updateScore(gameManager.getScore());
            handleMoveCompletion();
            handleLoseCondition();
        });
        MoveRight.setOnAction(event -> {
            gameManager.moveRight();
            updateStepCount(gameManager.getSteps());
            updateScore(gameManager.getScore());
            handleMoveCompletion();
            handleLoseCondition();
        });

        // 为按钮添加事件监听器
        Platform.runLater(() -> {
            gameViewCenter.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case W:
                        gameManager.moveUp();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        break;
                    case S:
                        gameManager.moveDown();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        break;
                    case A:
                        gameManager.moveLeft();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        break;
                    case D:
                        gameManager.moveRight();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        break;
                }
            });
        });
    }

    // 初始化游戏
    private void initialGame() {
        initializeStep();
        System.out.println("尝试启动游戏");
        gameManager = new GameManager();
        gameManager.setTimeUpdateListener(this::updateTimeDisplay); // 设置时间监听器
        // 使用 Timeline 定期更新时间显示
//        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
//            updateTimeDisplay(gameManager.getElapsedTime());
//        }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
        gameBoard = new GameBoard(gameManager);
        GameHasWon = false;
        //移除原来的GameBoard存在，则移除
        //将新的GameBoard显示在视图中心
        gameViewCenter.getChildren().add(gameBoard);

    }

    private void pauseSwitch() {
        if (Objects.equals(PauseButton.getText(), "暂停")) {
            gameManager.pauseGame();
            PauseButton.setText("继续");
        } else {
            gameManager.resumeGame();
            PauseButton.setText("暂停");
        }
    }


    private void updateTimeDisplay(long elapsedTime) {
        // 在 JavaFX 应用程序线程上更新 UI
        javafx.application.Platform.runLater(() -> {
            long seconds = elapsedTime / 1000;
            text_TimeNumber.setText(String.valueOf(seconds));
            if (status.getMode()==3&&elapsedTime>=status.getTargetTime()){
                showAlert("你输了", "游戏结束，时间到了");
                gameManager.stopGame();
            }
        });
    }

    public void handleMoveCompletion() { //检测游戏是否胜利
        if(GameHasWon==false) {//这个if保证了只弹出一次胜利界面
            if (gameManager.getMaxNumber() >= status.getTargetNumber()) {
                showAlert("恭喜!", "你真是数学天才，达到了8分!");
                GameHasWon = true;
            }
        }
    }

    public void handleLoseCondition(){
        if(gameManager.isGameOver()==true){
            showAlert("你输了", "游戏结束，你已经不能移动了");
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
                    gameManager.moveUp();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    break;
                case DOWN:
                    gameManager.moveDown();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    break;
                case LEFT:
                    gameManager.moveLeft();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
                    event.consume();
                    break;
                case RIGHT:
                    gameManager.moveRight();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
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
        PauseButton.setText("暂停");
        gameManager.restartGame();
    }
    public void updateStepCount(int steps) {
        text_step.setText("步数: " + steps);
    }

    public void updateScore(int score) {
        text_ScoreNumber.setText(String.valueOf(score));
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
