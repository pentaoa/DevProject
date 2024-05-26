package edu.sustech.students.ura.devproject.model;
/**
 * GameStatus
 * 游戏状态
 * 这个类用来存储游戏的常用信息，包括游戏模式、是否为在线游戏、玩家的名字等。
 * @version 1.0
 */

public class GameStatus {
    private static GameStatus instance;

    private int mode;
    private boolean isOnlineGame;
    private String playerName;
    private int targetScore;
    private int targetStep;
    private long targetTime = 10000;
    private int targetNumber = 2048;

    // 私有构造函数，防止外部实例化
    private GameStatus() {
    }

    // 获取唯一实例
    public static synchronized GameStatus getInstance() {
        if (instance == null) {
            instance = new GameStatus();
        }
        return instance;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
}
