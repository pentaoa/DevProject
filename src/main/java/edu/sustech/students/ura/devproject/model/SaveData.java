package edu.sustech.students.ura.devproject.model;

import java.io.Serial;
import java.io.Serializable;

public class SaveData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    int step;
    int[][] numbers = new int[4][4];
    int score;
    long time;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int[][] getNumbers() {
        int[][] number = new int[4][4];
        for(int row = 0 ;row<4;row++){
            for (int column = 0 ;column<4;column++){
                number[row][column]=this.numbers[row][column];
            }
        }
        return number;
    }

    public void setNumbers(int[][] numbers) {
        for(int row = 0 ;row<4;row++){
            for(int column = 0 ;column<4;column++){
                this.numbers[row][column]=numbers[row][column];
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
