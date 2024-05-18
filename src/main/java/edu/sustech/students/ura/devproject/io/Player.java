package edu.sustech.students.ura.devproject.io;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private  static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private int[][] gameBoard = new int[3][3];
    private int[][] location = new int[3][3];

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
