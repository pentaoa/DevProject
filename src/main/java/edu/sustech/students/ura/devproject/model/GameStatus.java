package edu.sustech.students.ura.devproject.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * GameStatus
 * 游戏状态
 * 这个类用来存储游戏的常用信息，包括游戏模式、是否为在线游戏、玩家的名字等。
 * @version 1.0
 */

public class GameStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static GameStatus instance;

    private int mode = 1;
    private boolean isOnlineGame = false;
    private String username;
    private int targetScore;
    private int targetStep;
    private long targetTime = 10000;
    private int targetNumber = 2048;
    private int[][] gridNumber;
    private int score = 0;
    private int step = 0;
    private long time = 0;

    // 私有构造函数，防止外部实例化
    private GameStatus() {
        gridNumber = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gridNumber[i][j] = 0;
            }
        }
    }

    // 获取唯一实例
    public static synchronized GameStatus getInstance() {
        if (instance == null) {
            instance = new GameStatus();
        }
        return instance;
    }

    // 设置唯一实例
    public static void setInstance(GameStatus newInstance) {
        instance = newInstance;
    }

    // Getter 和 Setter 方法
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isOnlineGame() {
        return isOnlineGame;
    }

    public void setOnlineGame(boolean onlineGame) {
        isOnlineGame = onlineGame;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(long targetTime) {
        this.targetTime = targetTime;
    }

    public int getTargetStep() {
        return targetStep;
    }

    public void setTargetStep(int targetStep) {
        this.targetStep = targetStep;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSteps() {
        return step;
    }

    public void setSteps(int step) {
        this.step = step;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setGridNumber(int[][] number) {
        for (int i=0;i<gridNumber.length;i++){
            for (int j=0;j<gridNumber[i].length;j++){
                gridNumber[i][j] = number[i][j];
            }
        }
    }

    public int[][] getGridNumber() {
        int[][] number = new int[4][4];
        for (int i=0;i<gridNumber.length;i++){
            for (int j=0;j<gridNumber[i].length;j++){
                number[i][j] = gridNumber[i][j];
            }
        }
        System.out.println("======status======");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(number[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("======status======");
        return number;
    }
}
