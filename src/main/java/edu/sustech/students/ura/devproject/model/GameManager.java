package edu.sustech.students.ura.devproject.model;

import edu.sustech.students.ura.devproject.controller.*;

/**
 * GameManager
 * 游戏管理器
 * 这个类是游戏的核心，负责管理游戏的进行，包括游戏的初始化、暂停、保存、读取等操作。
 * @version 1.0
 *
 * TODO: 在考虑以后要不要多一个 6x6 模式
 */

public class GameManager {
    public GridNumber grid;
    public GameManager(int mode) {
        // 新建游戏
        System.out.println("成功新建游戏！");
        initialGame();
    }

    // 初始化游戏
    private void initialGame() {
        grid = new GridNumber(4,4);
    }
    private void pauseGame() {
        // 暂停游戏
        System.out.println("暂停游戏······");
    }
    private void saveGame() {
        // 保存游戏
        System.out.println("保存游戏······");
    }
    private void loadGame() {
        // 读取游戏
        System.out.println("读取游戏······");
    }
    public GridNumber getGrid() {
        return grid;
    }
    public void remake(){
        grid.resetGrid();

    }
    public void EasyMode(){
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
}
