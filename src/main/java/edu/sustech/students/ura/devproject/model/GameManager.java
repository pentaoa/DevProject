package edu.sustech.students.ura.devproject.model;

import java.io.Serial;
import java.io.Serializable;
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

    public GridNumber grid;
    public int mode;
    private Timer timer;
    private long elapsedTime; // 以毫秒为单位
    private boolean isPaused;
    private Consumer<Long> timeUpdateListener;


    public GameManager() {
        // 新建游戏
        System.out.println("成功新建游戏！");
        initialGame();
    }

    // 初始化游戏
    private void initialGame() {
        GameStatus status = GameStatus.getInstance();
        mode = status.getMode();
        grid = new GridNumber(4,4);
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
        grid.resetGrid();
        if (mode == 1) {
            removeObstacles();
        }
        stopTimer();
        startTimer();
    }

    public GridNumber getGrid() {
        return grid;
    }

    public void removeObstacles(){
        for(int row = 0 ;row<grid.getNumbers().length;row++)
        {
            for(int column = 0 ; column<grid.getNumbers()[row].length;column++){
                if(grid.getNumbers()[row][column].get()==-1)
                {
                    grid.getNumbers()[row][column].setValue(0);
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
}
