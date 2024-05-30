package edu.sustech.students.ura.devproject.server;

import edu.sustech.students.ura.devproject.model.GameManager;

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
    private int[][] easyModeGameBoard = new int[4][4];
    private int[][] obstacleModeGameBoard = new int[4][4];
    private int[][] timeModeGameBoard = new int[4][4];

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

    public int[][] getEasyModeGameBoard() {
        return easyModeGameBoard;
    }

    public void setEasyModeGameBoard(int[][] easyModeGameBoard) {
        this.easyModeGameBoard = easyModeGameBoard;
    }

    public int[][] getObstacleModeGameBoard() {
        return obstacleModeGameBoard;
    }

    public void setObstacleModeGameBoard(int[][] obstacleModeGameBoard) {
        this.obstacleModeGameBoard = obstacleModeGameBoard;
    }

    public int[][] getTimeModeGameBoard() {
        return timeModeGameBoard;
    }

    public void setTimeModeGameBoard(int[][] timeModeGameBoard) {
        this.timeModeGameBoard = timeModeGameBoard;
    }
}
