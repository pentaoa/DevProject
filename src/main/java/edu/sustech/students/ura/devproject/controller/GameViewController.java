package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.Client;
import edu.sustech.students.ura.devproject.client.ClientManager;
import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.GameStatus;
import edu.sustech.students.ura.devproject.model.LocalSaveManager;
import edu.sustech.students.ura.devproject.util.Direction;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController{
    private int mode;
    private GameStatus status = GameStatus.getInstance();
    private GameManager gameManager;
    private GameBoard gameBoard;
    private boolean GameHasWon = false;
    private Client client = ClientManager.getClient();
    private LocalSaveManager localSaveManager = new LocalSaveManager();
//    private Timeline timeline;
    public Tile[][] tiles;

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
    private Label text_GameName;

    @FXML
    private ImageView gif_mona;


    @FXML
    private void initialize() throws InterruptedException {
        String modeName;
        switch (status.getMode()) {
            case 1:
                modeName = "经典模式";
                break;
            case 2:
                modeName = "障碍模式";
                break;
            case 3:
                modeName = "计时模式";
                break;
            default:
                modeName = "未知模式";
        }

        if(status.isOnlineGame()) {
            text_GameName.setText(status.getUsername()+": "+modeName);
        } else {
            text_GameName.setText("离线模式");
        }

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

        gif_mona.setOnMouseClicked(mouseEvent -> {
            long startTime = System.nanoTime(); // 记录开始时间用于计算循环间隔
            try {
                while (true) {
                    long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                    // 确保至少经过了一定的时间间隔
                    if (elapsedTime < 500) { // 500毫秒是半秒，控制ai的速度
                        Thread.sleep(500 - elapsedTime); // 等待半秒
                        startTime = System.nanoTime(); // 更新开始时间
                    }
                    // 尝试左下移动
                    gameManager.moveLeft();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
                    gameManager.moveDown();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    handleMoveCompletion();
                    handleLoseCondition();
                    // 检查是否还能继续左下移动
                    if (!gameManager.canMove(Direction.DOWN) && !gameManager.canMove(Direction.LEFT)) {
                        // 如果不能继续左下移动，执行一次上下移动
                        gameManager.moveUp();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        gameManager.moveDown();
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        // 再次检查是否可以恢复左下移动
                        if (!gameManager.canMove(Direction.DOWN) && !gameManager.canMove(Direction.LEFT)) {
                            break; // 无法继续移动，结束循环
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 保持中断状态
                System.out.println("Thread was interrupted");

            }
        });

        QuitButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("退出游戏");
            alert.setHeaderText("未保存的进度将会丢失");
            alert.setContentText("确定要退出游戏吗？");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    gameManager.stopGame();
                    if(status.isOnlineGame()) {
                        try {
                            // Load mode-view.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/mode-view.fxml"));
                            Scene modeScene = new Scene(loader.load());

                            // Get current stage and set scene
                            Stage stage = (Stage) QuitButton.getScene().getWindow();
                            stage.setScene(modeScene);
                            stage.setTitle("选择模式 | 2048");
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            // Load login-view.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/login-view.fxml"));
                            Scene loginScene = new Scene(loader.load());

                            // Get current stage and set scene
                            Stage stage = (Stage) QuitButton.getScene().getWindow();
                            stage.setScene(loginScene);
                            stage.setTitle("登录 | 2048");
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        });

        SaveButton.setOnAction(event -> {
            localSaveManager.save(gameManager);
        });

        LoadButton.setOnAction(event -> {
            localSaveManager.load();

            boolean allZero = true;
            for (int[] row : status.getGridNumber()) {
                for (int num : row) {
                    if (num != 0) {
                        allZero = false;
                        break;
                    }
                }
                if (!allZero) {
                    break;
                }
            }

            if (allZero) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("载入失败");
                alert.setHeaderText(null);
                alert.setContentText("载入的游戏数据无效");
                alert.showAndWait();
            } else {
                gameManager.setScore(status.getScore());
                gameManager.setSteps(status.getSteps());
                gameManager.setNumbers(status.getGridNumber());
                gameManager.setElapsedTime(status.getTime());
            }
        });

        RestartButton.setOnAction(event -> {
            PauseButton.setText("暂停");
            try {
                gameManager.restartGame();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            updateStepCount(0);
            updateScore(0);
        });

        PauseButton.setOnAction(event -> {
            pauseSwitch();
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
    private void initialGame() throws InterruptedException {
        initializeStep();
        System.out.println("尝试启动游戏");
        gameBoard = new GameBoard();
        gameManager = new GameManager(gameBoard);
        gameManager.setTimeUpdateListener(this::updateTimeDisplay); // 设置时间监听器
        // 使用 Timeline 定期更新时间显示
//        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
//            updateTimeDisplay(gameManager.getElapsedTime());
//        }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();

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
                showAlert("恭喜!", "你真是数学天才，达到了合成 "+status.getTargetNumber()+" 的目标！");
                GameHasWon = true;
            }
        }
    }

    public void handleLoseCondition(){
        if(gameManager.isGameOver()==true){
            showAlert("你输了", "游戏结束，你已经不能移动了");
            if (status.isOnlineGame()) {
                client.sendMessage("SET_HIGHSCORE:" + status.getUsername() + ":" + status.getMode() + ":" + gameManager.getScore());
            }
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

    public void updateStepCount(int steps) {
        text_step.setText("步数: " + steps);
    }

    public void updateScore(int score) {
        text_ScoreNumber.setText(String.valueOf(score));
    }
}
