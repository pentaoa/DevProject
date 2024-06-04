package edu.sustech.students.ura.devproject.model;

import edu.sustech.students.ura.devproject.controller.GameBoardInterface;
import edu.sustech.students.ura.devproject.util.AudioPlayer;
import edu.sustech.students.ura.devproject.util.Direction;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * GameManager
 * 游戏管理器
 * 这个类是游戏的核心，负责管理游戏的进行，包括游戏的初始化、暂停、保存、读取等操作。
 *
 * @version 1.0
 * <p>
 * TODO: 增加限时模式
 */

public class GameManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Timer timer;
    private Timer animationTimer;
    private long elapsedTime; // 以毫秒为单位
    private long animationTime;
    private boolean isPaused;
    private Consumer<Long> timeUpdateListener;
    private int score = 0;
    private int steps = 0;
    private final int X_COUNT = 4;
    private final int Y_COUNT = 4;
    private int obstacleNumber;
    private GameBoardInterface gameBoard;
    public GameStatus status = GameStatus.getInstance();

    public int[][] numbers;
    public int[][] rollBacknumbers = new int[4][4];
    private boolean whetherCanRollBack = true;
    private int scoreForrollback = 0;
    private ArrayList<int[][]> arrayListForRollBack =new ArrayList<int[][]>();

    static Random random = new Random();

    public GameManager() {

    }


    public GameManager(GameBoardInterface gameBoard)  {
        this.gameBoard = gameBoard;
        steps = status.getSteps();
        score = status.getScore();
        numbers = status.getGridNumber();
        initialNumbers();
        startTimer();
        System.out.println("成功新建游戏！");
    }

    private void startAnimationTimer() {
        animationTimer = new Timer();
        animationTime = 0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                animationTime += 100;
                System.out.println("Current animationTime: " + animationTime);
                if (animationTime >= 150) {
                    Platform.runLater(() -> {
                        System.out.println("动画结束");
                        updateTile();
                        animationTimer.cancel(); // 确保动画结束后取消定时器
                        animationTime = 0; // 重置 animationTime 仅在动画结束后
                    });
                }
            }
        };
        animationTimer.scheduleAtFixedRate(task, 0, 100);
    }


    // 开始计时
    private void startTimer() {
        timer = new Timer();
        elapsedTime = 0;
        isPaused = false;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    elapsedTime += 1000;
                    if (timeUpdateListener != null) {
                        timeUpdateListener.accept(elapsedTime);
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // 暂停计时
    private void pauseTimer() {
        isPaused = true;
    }

    // 恢复计时
    private void resumeTimer() {
        isPaused = false;
    }

    // 停止计时
    public void stopTimer() {
        timer.cancel();
    }

    public void pauseGame() {
        // 暂停游戏
        System.out.println("暂停游戏······");
        pauseTimer();
    }

    public void resumeGame() {
        // 恢复游戏
        System.out.println("恢复游戏······");
        resumeTimer();
    }

    public void stopGame() {
        // 停止游戏
        System.out.println("停止游戏");
        stopTimer();
    }

    private void saveGame() {
        // 保存游戏
        System.out.println("保存游戏······");
    }

    private void loadGame() {
        // 读取游戏
        System.out.println("读取游戏······");
    }

    public void restartGame() throws InterruptedException {
        // 重开游戏
        resetGrid();
        stopTimer();
        startTimer();
    }

    public void removeObstacles() {
        for (int row = 0; row < numbers.length; row++) {
            for (int column = 0; column < numbers[row].length; column++) {
                if (numbers[row][column] == -1) {
                    numbers[row][column] = 0;
                    gameBoard.setTileValue(row, column, 0);
                }
            }
        }
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setTimeUpdateListener(Consumer<Long> listener) {
        this.timeUpdateListener = listener;
    }

    public void placeRandomObstacle() {
        obstacleNumber=0;
        while (obstacleNumber == 0) {
            int row = random.nextInt(4);
            int column = random.nextInt(4);
            if (numbers[row][column] == 0) {
                numbers[row][column] = -1;
                gameBoard.setTileValue(row, column, -1);
                obstacleNumber++;
            }
        }
    }

    public void initialNumbers() {
        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT; column++) {
                if (gameBoard.haveTile(row, column)) {
                    gameBoard.removeTile(row, column);
                }
                gameBoard.createTile(row, column, numbers[row][column]);
            }
        }
        int initialNumber = 0;
        int whether4exist = 0;
        while (initialNumber < 2) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column] == 0 && whether4exist == 0) {
                numbers[row][column] = 4;
                gameBoard.setTileValue(row, column, 4);
                whether4exist++;
                initialNumber++;
            } else if (numbers[row][column] == 0 && whether4exist != 0) {
                numbers[row][column] = 2;
                gameBoard.setTileValue(row, column, 2);
                initialNumber++;
            }
        }
        this.steps = 0;
    }


    public void generateNewNumbers(GameManager model) {
        int newNumber = 0;
        while (newNumber == 0) {
            int row = random.nextInt(model.getX_COUNT());
            int column = random.nextInt(model.getY_COUNT());
            if (model.numbers[row][column] == 0) {
                model.numbers[row][column] = random.nextInt(2) == 0 ? 2 : 4;
                gameBoard.generateTile(row, column, model.numbers[row][column]);
                newNumber++;
            }
        }
    }

    public void fastGenerateNewNumbers(GameManager model) {
        int newNumber = 0;
        while (newNumber == 0) {
            int row = random.nextInt(model.getX_COUNT());
            int column = random.nextInt(model.getY_COUNT());
            if (model.numbers[row][column] == 0) {
                model.numbers[row][column] = random.nextInt(2) == 0 ? 2 : 4;
                newNumber++;
            }
        }
    }

    public void moveRight()  {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn--;
                } else if (numbers[row][column] == -1) {
                    targetColumn=column-1;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            for (int column = Y_COUNT - 1; column > 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column - 1] && numbers[row][column] != -1) {
                    final int fromRow = row, fromCol = column - 1, toCol = column;
                    numbers[row][column] *= 2;
                    numbers[row][column - 1] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, fromRow, toCol)));
                    moveSuccessfully = true;
                    column--;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn--;
                } else if (numbers[row][column] == -1) {
                    targetColumn=column-1;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }
    public void moveRightForHappy() throws InterruptedException {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn--;
                } else if (numbers[row][column] == -1) {
                    targetColumn--;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            for (int column = Y_COUNT - 1; column > 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column - 1] && numbers[row][column] != -1) {
                    final int fromRow = row, fromCol = column - 1, toCol = column;
                    numbers[row][column] *=numbers[row][column];
                    numbers[row][column - 1] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, fromRow, toCol)));
                    moveSuccessfully = true;
                    column--;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn--;
                } else if (numbers[row][column] == -1) {
                    targetColumn--;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }

    public void moveLeft(){
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn++;
                } else if (numbers[row][column] == -1) {
                    targetColumn=column+1;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT - 1; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column + 1] && numbers[row][column] != -1) {
                    final int fromRow = row, fromCol = column + 1, toCol = column;
                    numbers[row][column] *= 2;
                    numbers[row][column + 1] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, fromRow, toCol)));
                    moveSuccessfully = true;
                    column++;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn++;
                } else if (numbers[row][column] == -1) {
                    targetColumn=column+1;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }
    public void moveLeftForHappy() throws InterruptedException {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn++;
                } else if (numbers[row][column] == -1) {
                    targetColumn++;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT - 1; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column + 1] && numbers[row][column] != -1) {
                    final int fromRow = row, fromCol = column + 1, toCol = column;
                    numbers[row][column] *= numbers[row][column];
                    numbers[row][column + 1] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, fromRow, toCol)));
                    moveSuccessfully = true;
                    column++;
                }
            }
        }

        for (int row = 0; row < X_COUNT; row++) {
            int targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (column != targetColumn && numbers[row][targetColumn] == 0) {
                        final int fromRow = row, fromCol = column, toCol = targetColumn;
                        numbers[row][targetColumn] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, fromRow, toCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, fromRow, toCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetColumn++;
                } else if (numbers[row][column] == -1) {
                    targetColumn++;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }

    public void moveUp() {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow++;
                } else if (numbers[row][column] == -1) {
                    targetRow=row+1;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            for (int row = 0; row < X_COUNT - 1; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row + 1][column] && numbers[row][column] != -1) {
                    final int fromRow = row + 1, toRow = row, fromCol = column;
                    numbers[row][column] *= 2;
                    numbers[row + 1][column] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, toRow, fromCol)));
                    moveSuccessfully = true;
                    row++;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow++;
                } else if (numbers[row][column] == -1) {
                    targetRow=row+1;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }

    public void moveUpForHappy() throws InterruptedException {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow++;
                } else if (numbers[row][column] == -1) {
                    targetRow++;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            for (int row = 0; row < X_COUNT - 1; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row + 1][column] && numbers[row][column] != -1) {
                    final int fromRow = row + 1, toRow = row, fromCol = column;
                    numbers[row][column] *= numbers[row][column];
                    numbers[row + 1][column] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, toRow, fromCol)));
                    moveSuccessfully = true;
                    row++;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow++;
                } else if (numbers[row][column] == -1) {
                    targetRow++;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }
    public void moveDown() {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow--;
                } else if (numbers[row][column] == -1) {
                    targetRow=row-1;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            for (int row = X_COUNT - 1; row > 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row - 1][column] && numbers[row][column] != -1) {
                    final int fromRow = row - 1, toRow = row, fromCol = column;
                    numbers[row][column] *= 2;
                    numbers[row - 1][column] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, toRow, fromCol)));
                    moveSuccessfully = true;
                    row--;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow--;
                } else if (numbers[row][column] == -1) {
                    targetRow=row-1;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }
    public void moveDownForHappy() throws InterruptedException {
        if (status.isSoundOn == true) {
            AudioPlayer.playSound("src/main/resources/audio/move.wav");
        }
        boolean moveSuccessfully = false;
        Queue<Pair<int[], Runnable>> move1Queue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> mergeQueue = new LinkedList<>();
        Queue<Pair<int[], Runnable>> move2Queue = new LinkedList<>();

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move1Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow--;
                } else if (numbers[row][column] == -1) {
                    targetRow--;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            for (int row = X_COUNT - 1; row > 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] == numbers[row - 1][column] && numbers[row][column] != -1) {
                    final int fromRow = row - 1, toRow = row, fromCol = column;
                    numbers[row][column] *= numbers[row][column];
                    numbers[row - 1][column] = 0;
                    score += numbers[row][column];
                    mergeQueue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                            () -> gameBoard.mergeTile(fromRow, fromCol, toRow, fromCol)));
                    moveSuccessfully = true;
                    row--;
                }
            }
        }

        for (int column = 0; column < Y_COUNT; column++) {
            int targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                    if (row != targetRow && numbers[targetRow][column] == 0) {
                        final int fromRow = row, toRow = targetRow, fromCol = column;
                        numbers[targetRow][column] = numbers[row][column];
                        numbers[row][column] = 0;
                        move2Queue.add(new Pair<>(new int[]{fromRow, fromCol, toRow, fromCol},
                                () -> {
                                    try {
                                        gameBoard.moveTile(fromRow, fromCol, toRow, fromCol);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }));
                        moveSuccessfully = true;
                    }
                    targetRow--;
                } else if (numbers[row][column] == -1) {
                    targetRow--;
                }
            }
        }

        if (moveSuccessfully) {
            SequentialTransition sequence = new SequentialTransition();
            // Execute all move operations
            while (!move1Queue.isEmpty()) {
                Pair<int[], Runnable> moveAction = move1Queue.poll();
                moveAction.getValue().run();
            }
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> {
                // Execute all merge operations
                while (!mergeQueue.isEmpty()) {
                    Pair<int[], Runnable> mergeAction = mergeQueue.poll();
                    mergeAction.getValue().run();
                }
                PauseTransition laterPause = new PauseTransition(Duration.millis(100));
                laterPause.setOnFinished(e2 -> {
                    while(!move2Queue.isEmpty()) {
                        Pair<int[], Runnable> moveAction = move2Queue.poll();
                        moveAction.getValue().run();
                    }
                    PauseTransition lastPause = new PauseTransition(Duration.millis(100));
                    sequence.getChildren().add(lastPause);
                });
                sequence.getChildren().add(laterPause);
            });
            sequence.getChildren().add(pause);

            sequence.setOnFinished(e -> {
                // After all moves and merges, generate new numbers and update steps
                generateNewNumbers(this);
                steps++;
                printNumbers();
                startAnimationTimer();
            });

            sequence.play();
        }
    }

    public boolean isGameOver() {
        if (hasEmptyCells()) return false;
        return !canMove(Direction.UP) && !canMove(Direction.DOWN) && !canMove(Direction.LEFT) && !canMove(Direction.RIGHT);
    }

    private boolean hasEmptyCells() {
        for (int[] row : numbers) {
            for (int number : row) {
                if (number == 0) return true;
            }
        }
        return false;
    }

    public boolean canMove(Direction direction) {
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = 0; col < Y_COUNT; col++) {
                int current = numbers[row][col];
                if (current == 0) continue;
                switch (direction) {
                    case UP:
                        if (row > 0 && (numbers[row - 1][col] == 0 || numbers[row - 1][col] == current)) return true;
                        break;
                    case DOWN:
                        if (row < X_COUNT - 1 && (numbers[row + 1][col] == 0 || numbers[row + 1][col] == current))
                            return true;
                        break;
                    case LEFT:
                        if (col > 0 && (numbers[row][col - 1] == 0 || numbers[row][col - 1] == current)) return true;
                        break;
                    case RIGHT:
                        if (col < Y_COUNT - 1 && (numbers[row][col + 1] == 0 || numbers[row][col + 1] == current))
                            return true;
                        break;
                }
            }
        }
        return false;
    }

    public void resetGrid() throws InterruptedException {

        numbers = new int[X_COUNT][Y_COUNT];
        initialNumbers();
        steps = 0;
        score = 0;
    }

    public void printNumbers() {
        System.out.println();
        for (int[] row : numbers) {
            for (int number : row) {
                System.out.print(number + " ");
            }
            System.out.println();
        }
    }

    public int getMaxNumber() {
        int max = 0;
        int maxRow = 0;
        int maxCol = 0;
        for (int row = 0;row<4;row++) {
            for (int column = 0 ;column<4;column++) {
                if (numbers[row][column] > max) {
                    max = numbers[row][column];
                    maxRow = row;
                    maxCol = column;
                }
            }
        }
        if (max==65536) {
            numbers[maxRow][maxCol] = -1;
            updateTile();
            max = 0;
        }
        return max;
    }

    public void revise() {
        while (true) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column] != -1) {
                numbers[row][column] = 0;
                break;
            }
        }
        updateTile();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getX_COUNT() {
        return X_COUNT;
    }

    public int getY_COUNT() {
        return Y_COUNT;
    }

    public int getNumber(int row, int col) {
        return numbers[row][col];
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
        updateTile();
    }

    public void updateTile() {
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = 0; col < Y_COUNT; col++) {
                gameBoard.setTileValue(row, col, numbers[row][col]);
            }
        }
    }

    public int[][] getGridNumber() {
        return numbers;
    }

    public void copyForRollBack(){
        whetherCanRollBack = true;
        scoreForrollback=score;
        for(int row = 0 ;row<4;row++){
            for(int column = 0;column<4;column++){
                rollBacknumbers[row][column]=numbers[row][column];
            }
        }
    }
    public void rollBack() {
        if (whetherCanRollBack == true) {
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    numbers[row][column] = rollBacknumbers[row][column];

                }
            }
            score = scoreForrollback;
            steps--;
            whetherCanRollBack = false;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("撤回失败");
            alert.setHeaderText("一次只能撤回一步哦");
        }
    }
    public boolean getWhetherCanRollback(){
        return whetherCanRollBack;
    }
    public void copyForRollBackInfinitely(){
        int[][] transitNumbers = new int[4][4];
        for(int row = 0 ;row<4;row++){
            for (int column = 0;column<4;column++){
                transitNumbers[row][column]=numbers[row][column];
            }
        }
        arrayListForRollBack.add(transitNumbers);
    }
    public void rollBackInfinitely(){
        if(arrayListForRollBack.size()!=0) {
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    numbers[row][column] = arrayListForRollBack.get(arrayListForRollBack.size() - 1)[row][column]; //读取数组中的数据
                }
            }
            arrayListForRollBack.remove(arrayListForRollBack.size()-1);
        }
    }

    public ArrayList getArrayListForRollBackInfinitely(){
        return arrayListForRollBack;
    }

    // (ai功能）返回所有可能的下一步游戏状态
    public List<GameManager> getPossibleStates() {
        List<GameManager> possibleStates = new ArrayList<>();
        GameManager copyRight = this.copy();
        copyRight.fastMove(0);
        possibleStates.add(copyRight);
        GameManager copyLeft = this.copy();
        copyLeft.fastMove(1);
        possibleStates.add(copyLeft);
        GameManager copyUp = this.copy();
        copyUp.fastMove(2);
        possibleStates.add(copyUp);
        GameManager copyDown = this.copy();
        copyDown.fastMove(3);
        possibleStates.add(copyDown);
        return possibleStates;
    }

    // (AI功能）返回游戏的复刻
    public GameManager copy() {
        GameManager copy = new GameManager();
        // 复制所有的成员变量到新的实例中
        copy.numbers = new int[X_COUNT][Y_COUNT];
        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT; column++) {
                copy.numbers[row][column] = this.numbers[row][column];
            }
        }
        copy.score = this.score;
        copy.elapsedTime = this.elapsedTime;
        copy.steps = this.steps;
        return copy;
    }

    public void fastMove(int direction) {
        boolean moveSuccessfully = false;
        if (direction == 0) {
            System.out.println("moveRight");
            for (int row = 0; row < X_COUNT; row++) {
                int targetColumn = Y_COUNT - 1;
                for (int column = Y_COUNT - 1; column >= 0; column--) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (column != targetColumn && numbers[row][targetColumn] == 0) {
                            final int fromRow = row, fromCol = column, toCol = targetColumn;
                            numbers[row][targetColumn] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetColumn--;
                    } else if (numbers[row][column] == -1) {
                        targetColumn=column-1;
                    }
                }
            }

            for (int row = 0; row < X_COUNT; row++) {
                for (int column = Y_COUNT - 1; column > 0; column--) {
                    if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column - 1] && numbers[row][column] != -1) {
                        final int fromRow = row, fromCol = column - 1, toCol = column;
                        numbers[row][column] *= 2;
                        numbers[row][column - 1] = 0;
                        score += numbers[row][column];
                        moveSuccessfully = true;
                        column--;
                    }
                }
            }

            for (int row = 0; row < X_COUNT; row++) {
                int targetColumn = Y_COUNT - 1;
                for (int column = Y_COUNT - 1; column >= 0; column--) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (column != targetColumn && numbers[row][targetColumn] == 0) {
                            final int fromRow = row, fromCol = column, toCol = targetColumn;
                            numbers[row][targetColumn] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetColumn--;
                    } else if (numbers[row][column] == -1) {
                        targetColumn=column-1;
                    }
                }
            }

            if (moveSuccessfully) {
                fastGenerateNewNumbers(this);
                printNumbers();
            }
        }
        if (direction == 1) {
            System.out.println("moveLeft");
            // 执行 moveLeft 操作
            for (int row = 0; row < X_COUNT; row++) {
                int targetColumn = 0;
                for (int column = 0; column < Y_COUNT; column++) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (column != targetColumn && numbers[row][targetColumn] == 0) {
                            final int fromRow = row, fromCol = column, toCol = targetColumn;
                            numbers[row][targetColumn] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetColumn++;
                    } else if (numbers[row][column] == -1) {
                        targetColumn=column+1;
                    }
                }
            }

            for (int row = 0; row < X_COUNT; row++) {
                for (int column = 0; column < Y_COUNT - 1; column++) {
                    if (numbers[row][column] != 0 && numbers[row][column] == numbers[row][column + 1] && numbers[row][column] != -1) {
                        final int fromRow = row, fromCol = column + 1, toCol = column;
                        numbers[row][column] *= 2;
                        numbers[row][column + 1] = 0;
                        score += numbers[row][column];
                        moveSuccessfully = true;
                        column++;
                    }
                }
            }

            for (int row = 0; row < X_COUNT; row++) {
                int targetColumn = 0;
                for (int column = 0; column < Y_COUNT; column++) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (column != targetColumn && numbers[row][targetColumn] == 0) {
                            final int fromRow = row, fromCol = column, toCol = targetColumn;
                            numbers[row][targetColumn] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetColumn++;
                    } else if (numbers[row][column] == -1) {
                        targetColumn=column+1;
                    }
                }
            }

            if (moveSuccessfully) {
                fastGenerateNewNumbers(this);
                printNumbers();
            }
        }
        if (direction==2){
            System.out.println("moveUp");
            // 执行 moveUp 操作
            for (int column = 0; column < Y_COUNT; column++) {
                int targetRow = 0;
                for (int row = 0; row < X_COUNT; row++) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (row != targetRow && numbers[targetRow][column] == 0) {
                            final int fromRow = row, toRow = targetRow, fromCol = column;
                            numbers[targetRow][column] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetRow++;
                    } else if (numbers[row][column] == -1) {
                        targetRow=row+1;
                    }
                }
            }

            for (int column = 0; column < Y_COUNT; column++) {
                for (int row = 0; row < X_COUNT - 1; row++) {
                    if (numbers[row][column] != 0 && numbers[row][column] == numbers[row + 1][column] && numbers[row][column] != -1) {
                        final int fromRow = row + 1, toRow = row, fromCol = column;
                        numbers[row][column] *= 2;
                        numbers[row + 1][column] = 0;
                        score += numbers[row][column];
                        moveSuccessfully = true;
                        row++;
                    }
                }
            }

            for (int column = 0; column < Y_COUNT; column++) {
                int targetRow = 0;
                for (int row = 0; row < X_COUNT; row++) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (row != targetRow && numbers[targetRow][column] == 0) {
                            final int fromRow = row, toRow = targetRow, fromCol = column;
                            numbers[targetRow][column] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetRow++;
                    } else if (numbers[row][column] == -1) {
                        targetRow=row+1;
                    }
                }
            }

            if (moveSuccessfully) {
                fastGenerateNewNumbers(this);
                printNumbers();
            }
        }
        if(direction==3){
            System.out.println("moveDown");
            // 执行 moveDown 操作
            for (int column = 0; column < Y_COUNT; column++) {
                int targetRow = X_COUNT - 1;
                for (int row = X_COUNT - 1; row >= 0; row--) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (row != targetRow && numbers[targetRow][column] == 0) {
                            final int fromRow = row, toRow = targetRow, fromCol = column;
                            numbers[targetRow][column] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetRow--;
                    } else if (numbers[row][column] == -1) {
                        targetRow=row-1;
                    }
                }
            }
            for (int column = 0; column < Y_COUNT; column++) {
                for (int row = X_COUNT - 1; row > 0; row--) {
                    if (numbers[row][column] != 0 && numbers[row][column] == numbers[row - 1][column] && numbers[row][column] != -1) {
                        final int fromRow = row - 1, toRow = row, fromCol = column;
                        numbers[row][column] *= 2;
                        numbers[row - 1][column] = 0;
                        score += numbers[row][column];
                        moveSuccessfully = true;
                        row--;
                    }
                }
            }
            for (int column = 0; column < Y_COUNT; column++) {
                int targetRow = X_COUNT - 1;
                for (int row = X_COUNT - 1; row >= 0; row--) {
                    if (numbers[row][column] != 0 && numbers[row][column] != -1) {
                        if (row != targetRow && numbers[targetRow][column] == 0) {
                            final int fromRow = row, toRow = targetRow, fromCol = column;
                            numbers[targetRow][column] = numbers[row][column];
                            numbers[row][column] = 0;
                            moveSuccessfully = true;
                        }
                        targetRow--;
                    } else if (numbers[row][column] == -1) {
                        targetRow=row-1;
                    }
                }
            }
            if(moveSuccessfully){
                fastGenerateNewNumbers(this);
                printNumbers();
            }
        }
    }
}