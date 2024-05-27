package edu.sustech.students.ura.devproject.model;

import edu.sustech.students.ura.devproject.util.Direction;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * GameManager
 * 游戏管理器
 * 这个类是游戏的核心，负责管理游戏的进行，包括游戏的初始化、暂停、保存、读取等操作。
 * @version 1.0
 *
 * TODO: 增加限时模式
 */

public class GameManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public int mode;
    private Timer timer;
    private long elapsedTime; // 以毫秒为单位
    private boolean isPaused;
    private Consumer<Long> timeUpdateListener;
    private int score = 0;
    private int steps = 0;
    private final int X_COUNT = 4;
    private final int Y_COUNT = 4;
    private int obstacleNumber;

    public IntegerProperty[][] numbers;

    static Random random = new Random();

    public GameManager() {
        // 新建游戏
        System.out.println("成功新建游戏！");
        initialGame();
    }

    // 初始化游戏
    private void initialGame() {
        GameStatus status = GameStatus.getInstance();
        mode = status.getMode();
        numbers = new IntegerProperty[X_COUNT][Y_COUNT];
        initialNumbers();
        placeRandomObstacle();
        if (mode != 2) {
            removeObstacles();
        }
        startTimer();
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
    private void stopTimer() {
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

    public void restartGame(){
        // 重开游戏
        resetGrid();
        if (mode == 1) {
            removeObstacles();
        }
        stopTimer();
        startTimer();
    }

    public void removeObstacles(){
        for(int row = 0 ;row<numbers.length;row++)
        {
            for(int column = 0 ; column<numbers[row].length;column++){
                if(numbers[row][column].get()==-1)
                {
                    numbers[row][column].setValue(0);
                }
            }
        }
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setTimeUpdateListener(Consumer<Long> listener) {
        this.timeUpdateListener = listener;
    }

    public int getMode() {
        return mode;
    }

    private void placeRandomObstacle() {
        while (obstacleNumber == 0) {
            int row = random.nextInt(4);
            int column = random.nextInt(4);
            if (numbers[row][column].get() == 0) {
                numbers[row][column].setValue(-1);
                obstacleNumber++;
            }
        }
    }

    public void initialNumbers() {
        for (int row = 0; row < X_COUNT; row++) {
            for (int column = 0; column < Y_COUNT; column++) {
                numbers[row][column] = new SimpleIntegerProperty(0);
            }
        }
        int initialNumber = 0;
        int whether4exist = 0;
        while (initialNumber < 2) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column].get() == 0 && whether4exist == 0) {
                numbers[row][column].setValue(4);
                whether4exist++;
                initialNumber++;
            } else if (numbers[row][column].get() == 0 && whether4exist != 0) {
                numbers[row][column].setValue(2);
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
            if (model.numbers[row][column].getValue() == 0) {
                model.numbers[row][column].setValue(random.nextInt(2) == 0 ? 2 : 4);
                newNumber++;
            }
        }
    }

    public void moveRight() {
        boolean moveSuccessfully = false;
        for (int row = 0; row < X_COUNT; row++) {
            // Step 1: Move all non-zero elements to the right
            int targetColumn = Y_COUNT - 1; // The target column for the next non-zero element
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1) {// Only move if it's not already in the correct position
                            targetColumn=column;
                        }else if (numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn--;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int column = Y_COUNT - 1; column > 0; column--) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row][column - 1].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row][column - 1].setValue(0);
                    score +=numbers[row][column].get();
                    moveSuccessfully = true;
                    column--; // Skip the next column since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetColumn = Y_COUNT - 1;
            for (int column = Y_COUNT - 1; column >= 0; column--) {
                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1) {// Only move if it's not already in the correct position
                            targetColumn=column;
                        }else if (numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn--;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveLeft() {
        boolean moveSuccessfully = false;
        for (int row = 0; row < X_COUNT; row++) {
            // Step 1: Move all non-zero elements to the left
            int targetColumn = 0; // The target column for the next non-zero element
            for (int column = 0; column < Y_COUNT; column++) {

                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetColumn = column;
                        }
                        else if(numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn++;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int column = 0; column < Y_COUNT - 1; column++) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row][column + 1].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row][column + 1].setValue(0);
                    moveSuccessfully = true;
                    score +=numbers[row][column].get();
                    column++; // Skip the next column since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetColumn = 0;
            for (int column = 0; column < Y_COUNT; column++) {

                if (numbers[row][column].get() != 0) {
                    if (column != targetColumn) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetColumn = column;
                        }
                        else if(numbers[row][targetColumn].get()==0){
                            numbers[row][targetColumn].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetColumn++;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveUp() {
        boolean moveSuccessfully = false;
        for (int column = 0; column < Y_COUNT; column++) {
            // Step 1: Move all non-zero elements upwards
            int targetRow = 0; // The target row for the next non-zero element
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow++;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int row = 0; row < X_COUNT - 1; row++) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row + 1][column].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row + 1][column].setValue(0);
                    moveSuccessfully = true;
                    score +=numbers[row][column].get();
                    row++; // Skip the next row since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetRow = 0;
            for (int row = 0; row < X_COUNT; row++) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow++;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;
        }
    }

    public void moveDown() {
        boolean moveSuccessfully = false;
        for (int column = 0; column < Y_COUNT; column++) {
            // Step 1: Move all non-zero elements downwards
            int targetRow = X_COUNT - 1; // The target row for the next non-zero element
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow--;
                }
            }
            // Step 2: Merge adjacent elements if they are equal
            for (int row = X_COUNT - 1; row > 0; row--) {
                if (numbers[row][column].get() != 0 && numbers[row][column].get() == numbers[row - 1][column].get()) {
                    numbers[row][column].setValue(numbers[row][column].get() * 2);
                    numbers[row - 1][column].setValue(0);
                    moveSuccessfully = true;
                    score +=numbers[row][column].get();
                    row--; // Skip the next row since it's already merged
                }
            }
            // Step 3: Move again to fill the gaps created by merging
            targetRow = X_COUNT - 1;
            for (int row = X_COUNT - 1; row >= 0; row--) {
                if (numbers[row][column].get() != 0) {
                    if (row != targetRow) {
                        if(numbers[row][column].get()==-1){// Only move if it's not already in the correct position
                            targetRow = row;
                        }
                        else if (numbers[targetRow][column].get()==0){
                            numbers[targetRow][column].setValue(numbers[row][column].get());
                            numbers[row][column].setValue(0);
                            moveSuccessfully = true;
                        }
                    }
                    targetRow--;
                }
            }
        }
        if (moveSuccessfully) {
            generateNewNumbers(this);
            steps++;

        }
    }

    public boolean isGameOver() {
        if (hasEmptyCells()) return false;
        return !canMove(Direction.UP) && !canMove(Direction.DOWN) && !canMove(Direction.LEFT) && !canMove(Direction.RIGHT);
    }

    private boolean hasEmptyCells() {
        for (IntegerProperty[] row : numbers) {
            for (IntegerProperty cell : row) {
                if (cell.get() == 0) return true;
            }
        }
        return false;
    }

    private boolean canMove(Direction direction) {
        for (int row = 0; row < X_COUNT; row++) {
            for (int col = 0; col < Y_COUNT; col++) {
                int current = numbers[row][col].get();
                if (current == 0) continue;
                switch (direction) {
                    case UP:
                        if (row > 0 && (numbers[row - 1][col].get() == 0 || numbers[row - 1][col].get() == current)) return true;
                        break;
                    case DOWN:
                        if (row < X_COUNT - 1 && (numbers[row + 1][col].get() == 0 || numbers[row + 1][col].get() == current)) return true;
                        break;
                    case LEFT:
                        if (col > 0 && (numbers[row][col - 1].get() == 0 || numbers[row][col - 1].get() == current)) return true;
                        break;
                    case RIGHT:
                        if (col < Y_COUNT - 1 && (numbers[row][col + 1].get() == 0 || numbers[row][col + 1].get() == current)) return true;
                        break;
                }
            }
        }
        return false;
    }

    public void resetGrid() {
        numbers = new IntegerProperty[X_COUNT][Y_COUNT];
        initialNumbers();
        placeRandomObstacle();
        steps = 0;
        score = 0;
    }

    public void printNumbers() {
        for (IntegerProperty[] row : numbers) {
            for (IntegerProperty cell : row) {
                System.out.print(cell.get() + " ");
            }
            System.out.println();
        }
    }

    public int getMaxNumber() {
        int max = 0;
        for (IntegerProperty[] row : numbers) {
            for (IntegerProperty cell : row) {
                if (cell.get() > max) max = cell.get();
            }
        }
        return max;
    }

    public void revise() {
        while (true) {
            int row = random.nextInt(X_COUNT);
            int column = random.nextInt(Y_COUNT);
            if (numbers[row][column].get() != -1) {
                numbers[row][column].setValue(0);
                break;
            }
        }
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

    public IntegerProperty getNumber(int row, int col) {
        return numbers[row][col];
    }

    public IntegerProperty[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(IntegerProperty[][] numbers) {
        this.numbers = numbers;
    }
}