package edu.sustech.students.ura.devproject.server;

import edu.sustech.students.ura.devproject.model.GameManager;
import edu.sustech.students.ura.devproject.model.SaveData;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String ID;
    private String username;
    private String password;
    private int easyModeHighScore = 0;
    private int timeModeHighScore = 0;
    private int obstacleModeHighScore = 0;
    private SaveData classicalSaveData = new SaveData();
    private SaveData obstacleSaveData = new SaveData();
    private SaveData timeSaveData = new SaveData();

    public GameManager getTimeGameManager() {
        return timeGameManager;
    }

    public SaveData getTimeSaveData() {
        return timeSaveData;
    }

    public void setTimeSaveData(SaveData timeSaveData) {
        this.timeSaveData = timeSaveData;
    }

    public SaveData getObstacleSaveData() {
        return obstacleSaveData;
    }

    public void setObstacleSaveData(SaveData obstacleSaveData) {
        this.obstacleSaveData = obstacleSaveData;
    }

    public SaveData getClassicalSaveData() {
        return classicalSaveData;
    }

    public void setClassicalSaveData(SaveData classicalSaveData) {
        this.classicalSaveData = classicalSaveData;
    }

    public void setTimeGameManager(GameManager timeGameManager) {
        this.timeGameManager = timeGameManager;
    }

    public GameManager getObstacleGameManager() {
        return obstacleGameManager;
    }

    public void setObstacleGameManager(GameManager obstacleGameManager) {
        this.obstacleGameManager = obstacleGameManager;
    }

    public GameManager getClassicalGameManager() {
        return classicalGameManager;
    }

    public void setClassicalGameManager(GameManager classicalGameManager) {
        this.classicalGameManager = classicalGameManager;
    }

    private GameManager classicalGameManager = new GameManager();
    private GameManager timeGameManager = new GameManager();
    private GameManager obstacleGameManager = new GameManager();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getObstacleModeHighScore() {
        return obstacleModeHighScore;
    }

    public void setObstacleModeHighScore(int obstacleModeHighScore) {
        this.obstacleModeHighScore = obstacleModeHighScore;
    }

    public int getTimeModeHighScore() {
        return timeModeHighScore;
    }

    public void setTimeModeHighScore(int timeModeHighScore) {
        this.timeModeHighScore = timeModeHighScore;
    }

    public int getEasyModeHighScore() {
        return easyModeHighScore;
    }

    public void setEasyModeHighScore(int easyModeHighScore) {
        this.easyModeHighScore = easyModeHighScore;
    }
}
