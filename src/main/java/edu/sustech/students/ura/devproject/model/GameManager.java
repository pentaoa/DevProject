package edu.sustech.students.ura.devproject.model;

/**
 * GameManager
 * 游戏管理器
 * @version 1.0
 * 这个类是游戏的核心，负责管理游戏的进行，包括游戏的初始化、暂停、保存、读取等操作。
 *
 * TODO: 在考虑以后要不要多一个 6x6 模式
 */

public class GameManager {
    private Gridnumber model;
    public GameManager() {
        // 新建游戏
        System.out.println("成功新建游戏！");
    }

    private void initialGame() {
        // 初始化游戏
        System.out.println("初始化游戏······");
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

    private void doMove() {
        // 执行移动
        System.out.println("执行移动······");
    }
}
