package edu.sustech.students.ura.devproject.model;

import java.io.Serializable;

public class SaveData implements Serializable {
    private int[][] board;
    private int score;

    // Constructors, getters, and setters
    public SaveData() {
        this.board = new int[4][4];
        this.score = 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}