package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.client.Client;
import edu.sustech.students.ura.devproject.client.ClientListener;
import edu.sustech.students.ura.devproject.client.ClientManager;
import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.GameStatus;
import edu.sustech.students.ura.devproject.model.LocalSaveManager;
import edu.sustech.students.ura.devproject.model.SaveData;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Base64;
import java.util.Objects;

/**
 * GameController
 * 游戏界面控制器
 * 这是 2048 游戏的主界面，用户在这里可以看到一个方格盘，叫做 GridPane, 以及一些按钮，用户可以通过这些按钮来控制游戏的进行。
 * 注意：每个元素都有自己的 ID，这个 ID 是 FXML 文件中定义的。在这个类中，我们可以通过 @FXML 注解来获取这些元素。
 *
 * @version 1.0
 */
public class GameViewController implements ClientListener {
    private int mode;
    private boolean whetherCanMove = true;
    private GameStatus status = GameStatus.getInstance();
    private GameManager gameManager;
    private GameBoard gameBoard;
    private boolean GameHasWon = false;
    private Client client;
    private LocalSaveManager localSaveManager = new LocalSaveManager();
    //    private Timeline timeline;
    public Tile[][] tiles;
    private long lastInputTime = 0;
    Word word = Word.getInstance();

    @FXML
    private Button RollBackButton;
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
    private BorderPane rootpane;

    @FXML
    private Button button_setTheme;

    @FXML
    private void initialize() throws InterruptedException {
        client = ClientManager.getClient();
        client.setListener(this);
        iniTheme();
        whetherCanMove = true;
        String modeName;
        switch (status.getMode()) {
            case 1:
                modeName = "经典模式";
                break;
            case 2:
                modeName = "障碍模式";
                word.updateWord("这个障碍模式可\n能会有点难哦");
                break;
            case 3:
                modeName = "计时模式";
                word.updateWord("看看你在规定时间\n内能拿多少分");
                break;
            case 4:
                modeName = "作弊模式";
                word.updateWord("游戏可以作弊，\n期末考试不能");
                break;
            case 5:
                modeName = "欢乐模式";
                word.updateWord("骗骗cheems就行了，\n别骗自己");
                break;
            default:
                modeName = "未知模式";
        }

        if (status.isOnlineGame()) {
            text_GameName.setText(status.getUsername() + ": " + modeName);
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

        GameAi ai = new GameAi(gameManager);

        gif_mona.setOnMouseClicked(mouseEvent -> {
            int direction = ai.move();
            if (whetherCanMove == true) {
                switch (direction) {
                    case 1 -> {
                        if (status.getMode() != 5) {
                            gameManager.moveRight();
                        } else {
                            try {
                                gameManager.moveRightForHappy();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        word.updateWord("听我的，向右走");
                    }
                    case 2 -> {
                        if (status.getMode() != 5) {
                            gameManager.moveLeft();
                        } else {
                            try {
                                gameManager.moveLeftForHappy();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        word.updateWord("听我的，向左走");
                    }
                    case 3 -> {
                        if (status.getMode() != 5) {
                            gameManager.moveUp();
                        } else {
                            try {
                                gameManager.moveUpForHappy();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        word.updateWord("听我的，向上走");
                    }
                    case 4 -> {
                        if (status.getMode() != 5) {
                            gameManager.moveDown();
                        } else {
                            try {
                                gameManager.moveDownForHappy();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        word.updateWord("听我的，向下走");
                    }
                    case -1 -> {
                        System.out.println("AI 无法找到有效移动");
                        word.updateWord("我也不知道走哪好了");
                    }
                }
                updateStepCount(gameManager.getSteps());
                updateScore(gameManager.getScore());
                handleMoveCompletion();
                handleLoseCondition();
            } else {
                showAlert("不许动", "你不能再移动了");
            }
        });


        button_setTheme.setOnAction(event -> {
            status.theme++;
            if (status.theme == 4) {
                status.theme = 0;
            }
            updateTheme();
        });

        QuitButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("退出游戏");
            alert.setHeaderText("未保存的进度将会丢失");
            alert.setContentText("确定要退出游戏吗？");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    gameManager.stopGame();
                    status.initialNumber();
                    if (status.isOnlineGame()) {
                        try {
                            // Load mode-view.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/sustech/students/ura/devproject/user-view.fxml"));
                            Scene modeScene = new Scene(loader.load());

                            // Get current stage and set scene
                            Stage stage = (Stage) QuitButton.getScene().getWindow();
                            stage.setScene(modeScene);
                            stage.setTitle("用户总览 | 2048");
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

        RollBackButton.setOnAction(event -> {
            if (status.getSteps() != 0) {
                if (status.getMode() != 4) {
                    if (gameManager.getWhetherCanRollback() == false) {
                        showAlert("撤回失败", "一次只能撤回一步哦");
                    }
                    gameManager.rollBack();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    gameManager.printNumbers();
                    gameManager.updateTile();
                } else if (gameManager.getArrayListForRollBackInfinitely().size() != 0) {
                    gameManager.rollBackInfinitely();
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                    gameManager.printNumbers();
                    gameManager.updateTile();
                } else {
                    showAlert("撤回失败", "你已经回到最初的起点了");
                }
            } else {
                showAlert("撤回失败", "你已经回到最初的起点了");
            }
        });

        SaveButton.setOnAction(event -> {
            if (status.isOnlineGame() == true) {
                gameManager.save();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    objectOutputStream.writeObject(gameManager.saveData);
                    objectOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String message = Base64.getEncoder().encodeToString(bytes);
                client.sendMessage("SAVE:" + status.getUsername() + ":" + status.getMode() + ":" + message);
            } else {
                localSaveManager.save(gameManager);
            }
        });

        LoadButton.setOnAction(event -> {
            if (status.isOnlineGame()) {
                client.sendMessage("LOAD:" + status.getUsername() + ":" + status.getMode());
            } else {
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
            }
        });

        RestartButton.setOnAction(event -> {
            whetherCanMove = true;
            PauseButton.setText("暂停");
            try {
                gameManager.restartGame();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (status.getMode() == 2) {
                gameManager.placeRandomObstacle();
            }
            updateStepCount(0);
            updateScore(0);
        });

        PauseButton.setOnAction(event -> {
            pauseSwitch();
        });


        MoveUp.setOnAction(event -> {
            if (whetherCanMove == true) {// 每次移动之后，检查胜利和失败
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastInputTime < 200) {
                    return;
                }
                try {
                    if (status.getMode() != 4) {
                        gameManager.copyForRollBack();
                    } else {
                        gameManager.copyForRollBackInfinitely();
                    }
                    if (status.getMode() == 5) {
                        gameManager.moveUpForHappy();
                    } else gameManager.moveUp();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                updateStepCount(gameManager.getSteps());
                updateScore(gameManager.getScore());
                handleMoveCompletion();
                handleLoseCondition();
                lastInputTime = currentTime;
            } else {
                showAlert("不许动", "你不能再移动了");
            }
        });
        MoveDown.setOnAction(event -> {
            if (whetherCanMove == true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastInputTime < 200) {
                    return;
                }
                try {
                    if (status.getMode() != 4) {
                        gameManager.copyForRollBack();
                    } else {
                        gameManager.copyForRollBackInfinitely();
                    }
                    if (status.getMode() == 5) {
                        gameManager.moveDownForHappy();
                    } else gameManager.moveDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                updateStepCount(gameManager.getSteps());
                updateScore(gameManager.getScore());
                handleMoveCompletion();
                handleLoseCondition();
                lastInputTime = currentTime;
            } else {
                showAlert("不许动", "你不能再移动了");
            }
        });
        MoveLeft.setOnAction(event -> {
            if (whetherCanMove == true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastInputTime < 200) {
                    return;
                }
                try {
                    if (status.getMode() != 4) {
                        gameManager.copyForRollBack();
                    } else {
                        gameManager.copyForRollBackInfinitely();
                    }
                    if (status.getMode() == 5) {
                        gameManager.moveLeftForHappy();
                    } else gameManager.moveLeft();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                updateStepCount(gameManager.getSteps());
                updateScore(gameManager.getScore());
                handleMoveCompletion();
                handleLoseCondition();
                lastInputTime = currentTime;
            } else {
                showAlert("不许动", "你不能再移动了");
            }
        });
        MoveRight.setOnAction(event -> {
            if (whetherCanMove == true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastInputTime < 200) {
                    return;
                }
                try {
                    if (status.getMode() != 4) {
                        gameManager.copyForRollBack();
                    } else {
                        gameManager.copyForRollBackInfinitely();
                    }
                    if (status.getMode() == 5) {
                        gameManager.moveRightForHappy();
                    } else gameManager.moveRight();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                updateStepCount(gameManager.getSteps());
                updateScore(gameManager.getScore());
                handleMoveCompletion();
                handleLoseCondition();
                lastInputTime = currentTime;
            } else {
                showAlert("不许动", "你不能再移动了");
            }
        });

        // 为按钮添加事件监听器
        Platform.runLater(() -> {
            gameViewCenter.getScene().setOnKeyPressed(event -> {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastInputTime < 200) {
                    return;
                }
                if (whetherCanMove == true) {
                    switch (event.getCode()) {
                        case W:
                            try {
                                if (status.getMode() != 4) {
                                    gameManager.copyForRollBack();
                                } else {
                                    gameManager.copyForRollBackInfinitely();
                                }
                                if (status.getMode() == 5) {
                                    gameManager.moveUpForHappy();
                                } else gameManager.moveUp();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            updateStepCount(gameManager.getSteps());
                            updateScore(gameManager.getScore());
                            handleMoveCompletion();
                            handleLoseCondition();
                            break;
                        case S:
                            try {
                                if (status.getMode() != 4) {
                                    gameManager.copyForRollBack();
                                } else {
                                    gameManager.copyForRollBackInfinitely();
                                }
                                if (status.getMode() == 5) {
                                    gameManager.moveDownForHappy();
                                } else gameManager.moveDown();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            updateStepCount(gameManager.getSteps());
                            updateScore(gameManager.getScore());
                            handleMoveCompletion();
                            handleLoseCondition();
                            break;
                        case A:
                            try {
                                if (status.getMode() != 4) {
                                    gameManager.copyForRollBack();
                                } else {
                                    gameManager.copyForRollBackInfinitely();
                                }
                                if (status.getMode() == 5) {
                                    gameManager.moveLeftForHappy();
                                } else gameManager.moveLeft();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            updateStepCount(gameManager.getSteps());
                            updateScore(gameManager.getScore());
                            handleMoveCompletion();
                            handleLoseCondition();
                            break;
                        case D:
                            try {
                                if (status.getMode() != 4) {
                                    gameManager.copyForRollBack();
                                } else {
                                    gameManager.copyForRollBackInfinitely();
                                }
                                if (status.getMode() == 5) {
                                    gameManager.moveRightForHappy();
                                } else gameManager.moveRight();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            updateStepCount(gameManager.getSteps());
                            updateScore(gameManager.getScore());
                            handleMoveCompletion();
                            handleLoseCondition();
                            break;
                    }
                    lastInputTime = currentTime;
                } else {
                    showAlert("不许动", "你不能再移动了");
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

        GameHasWon = false;
        //移除原来的GameBoard存在，则移除
        //将新的GameBoard显示在视图中心

        gameViewCenter.getChildren().add(gameBoard);
        if (status.getMode() == 2) {
            gameManager.placeRandomObstacle();
        }
    }

    private void pauseSwitch() {
        if (Objects.equals(PauseButton.getText(), "暂停")) {
            gameManager.pauseGame();
            whetherCanMove = false;
            PauseButton.setText("继续");
        } else {
            gameManager.resumeGame();
            whetherCanMove = true;
            PauseButton.setText("暂停");
        }
    }


    private void updateTimeDisplay(long elapsedTime) {
        // 在 JavaFX 应用程序线程上更新 UI
        javafx.application.Platform.runLater(() -> {
            if (elapsedTime % 10000 == 0) {
                localSaveManager.autoSave(gameManager);
                if (status.isOnlineGame() == true) {
                    gameManager.save();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = null;
                    try {
                        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        objectOutputStream.writeObject(gameManager.saveData);
                        objectOutputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String message = Base64.getEncoder().encodeToString(bytes);
                    client.sendMessage("SAVE:" + status.getUsername() + ":" + status.getMode() + ":" + message);
                }
            }

            long seconds = elapsedTime / 1000;
            text_TimeNumber.setText(String.valueOf(seconds));
            if (status.getMode() == 3 && elapsedTime >= status.getTargetTime()) {
                gameManager.stopTimer();
                showAlert("你输了", "游戏结束，时间到了");
                gameManager.stopGame();
                whetherCanMove = false;
            }
        });
    }

    public void handleMoveCompletion() { //检测游戏是否胜利
        if (GameHasWon == false) {//这个if保证了只弹出一次胜利界面
            if (gameManager.getMaxNumber() >= status.getTargetNumber()) {
                if (status.isSoundOn) {
                    AudioPlayer.playSound("src/main/resources/audio/win.wav");
                }
                showAlert("恭喜!", "你真是数学天才，达到了合成 " + status.getTargetNumber() + " 的目标！");
                word.updateWord("你是怎么玩到" + status.getTargetNumber() + "的？🤬");
                GameHasWon = true;
            }
        }
    }

    public void handleLoseCondition() {
        if (gameManager.isGameOver() == true) {
            if (status.isSoundOn) {
                AudioPlayer.playSound("src/main/resources/audio/fail.wav");
            }
            word.updateWord("失败乃成功之母，再来一次吧");
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("你输了");
            alert.setHeaderText("但是可以复活！");
            alert.setContentText("是否充值以获取复活机会？");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    gameManager.revise();
                } else if (response == ButtonType.CANCEL) {
                    if (status.isOnlineGame()) {
                        client.sendMessage("SET_HIGHSCORE:" + status.getUsername() + ":" + status.getMode() + ":" + gameManager.getScore());
                    }
                    gameManager.stopGame();
                }
            });
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
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastInputTime < 200) {
                return;
            }
            if (whetherCanMove == true) {
                switch (event.getCode()) {
                    case UP:
                        try {
                            if (status.getMode() != 4) {
                                gameManager.copyForRollBack();
                            } else {
                                gameManager.copyForRollBackInfinitely();
                            }
                            if (status.getMode() == 5) {
                                gameManager.moveUpForHappy();
                            } else gameManager.moveUp();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        event.consume();
                        break;
                    case DOWN:
                        try {
                            if (status.getMode() != 4) {
                                gameManager.copyForRollBack();
                            } else {
                                gameManager.copyForRollBackInfinitely();
                            }
                            if (status.getMode() == 5) {
                                gameManager.moveDownForHappy();
                            } else gameManager.moveDown();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        event.consume();
                        break;
                    case LEFT:
                        try {
                            if (status.getMode() != 4) {
                                gameManager.copyForRollBack();
                            } else {
                                gameManager.copyForRollBackInfinitely();
                            }
                            if (status.getMode() == 5) {
                                gameManager.moveLeftForHappy();
                            } else {
                                gameManager.moveLeft();
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        event.consume();
                        break;
                    case RIGHT:
                        try {
                            if (status.getMode() != 4) {
                                gameManager.copyForRollBack();
                            } else {
                                gameManager.copyForRollBackInfinitely();
                            }
                            if (status.getMode() == 5) {
                                gameManager.moveRightForHappy();
                            } else gameManager.moveRight();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        updateStepCount(gameManager.getSteps());
                        updateScore(gameManager.getScore());
                        handleMoveCompletion();
                        handleLoseCondition();
                        event.consume();
                        break;
                    default:
                        break;
                }
                lastInputTime = currentTime;
            } else {
                showAlert("不许动", "你不能再移动了");
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

    public void updateTheme() {
        if (status.theme == 3) {
            rootpane.setStyle("-fx-background-color: #211d1d");
            gameViewCenter.setStyle("-fx-background-color: #36302c");
            text_ScoreNumber.setStyle("-fx-text-fill: #ffffff");
            text_TimeNumber.setStyle("-fx-text-fill: #ffffff");
            text_score.setStyle("-fx-text-fill: #ffffff");
            text_step.setStyle("-fx-text-fill: #ffffff");
            text_time.setStyle("-fx-text-fill: #ffffff");
            text_GameName.setStyle("-fx-text-fill: #ffffff");
        } else if (status.theme == 0) {
            rootpane.setStyle("-fx-background-color: #faf8ef");
            gameViewCenter.setStyle("-fx-background-color: BDB3A9B2");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        } else if (status.theme == 1) {
            rootpane.setStyle("-fx-background-color: #c0ece8");
            gameViewCenter.setStyle("-fx-background-color: #66c0b8");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        } else if (status.theme == 2) {
            rootpane.setStyle("-fx-background-color: #f7f7f7");
            gameViewCenter.setStyle("-fx-background-color: #7c7c7c");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        }
//        gameBoard.initiate();
//        gameManager.updateTile();
    }

    public void iniTheme() {
        if (status.theme == 3) {
            rootpane.setStyle("-fx-background-color: #211d1d");
            gameViewCenter.setStyle("-fx-background-color: #36302c");
            text_ScoreNumber.setStyle("-fx-text-fill: #ffffff");
            text_TimeNumber.setStyle("-fx-text-fill: #ffffff");
            text_score.setStyle("-fx-text-fill: #ffffff");
            text_step.setStyle("-fx-text-fill: #ffffff");
            text_time.setStyle("-fx-text-fill: #ffffff");
            text_GameName.setStyle("-fx-text-fill: #ffffff");
        } else if (status.theme == 0) {
            rootpane.setStyle("-fx-background-color: #faf8ef");
            gameViewCenter.setStyle("-fx-background-color: BDB3A9B2");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        } else if (status.theme == 1) {
            rootpane.setStyle("-fx-background-color: #c0ece8");
            gameViewCenter.setStyle("-fx-background-color: #66c0b8");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        } else if (status.theme == 2) {
            rootpane.setStyle("-fx-background-color: #f7f7f7");
            gameViewCenter.setStyle("-fx-background-color: #7c7c7c");
            text_ScoreNumber.setStyle("-fx-text-fill: #776e65");
            text_TimeNumber.setStyle("-fx-text-fill: #776e65");
            text_score.setStyle("-fx-text-fill: #776e65");
            text_step.setStyle("-fx-text-fill: #776e65");
            text_time.setStyle("-fx-text-fill: #776e65");
            text_GameName.setStyle("-fx-text-fill: #776e65");
        }
    }

    @Override
    public void onMessageReceived(String message) {
        // Update UI in JavaFX application thread
        Platform.runLater(() -> {
            if (message.startsWith("LOAD_SUCCESS")) {
                System.out.println("尝试读取");
                String[] parts = message.split(":");
                String base64 = parts[1];
                try {
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    SaveData saveData = (SaveData) objectInputStream.readObject();
                    System.out.println("加载到的分数是："+saveData.getScore());
                    this.gameManager.setSteps(saveData.getStep());
                    this.gameManager.setNumbers(saveData.getNumbers());
                    this.gameManager.setElapsedTime(saveData.getTime());
                    this.gameManager.setScore(saveData.getScore());
                    updateStepCount(gameManager.getSteps());
                    updateScore(gameManager.getScore());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (message.startsWith("LOAD_FAIL")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("载入失败");
                alert.setHeaderText(null);
                alert.setContentText("载入的游戏数据无效");
                alert.showAndWait();
            } else if (message.startsWith("SAVE_SUCCESS")) {
                text_GameName.setText("游戏进度已保存");
            } else if (message.startsWith("SAVE_FAIL")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("保存失败");
                alert.setHeaderText(null);
                alert.setContentText("游戏进度保存失败");
                alert.showAndWait();
            }
        });
    }
}
