package edu.sustech.students.ura.devproject.server;

import edu.sustech.students.ura.devproject.model.GameManager;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private int easyModeHighScore = 0;
    private int timeModeHighScore = 0;
    private int obstacleModeHighScore = 0;
    private GameManager currentGame;


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

    public GameManager getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameManager currentGame) {
        this.currentGame = currentGame;
    }
}
