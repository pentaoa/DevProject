package edu.sustech.students.ura.devproject.model;

/**
 * Block
 * 块
 * @version 1.0
 * 代表游戏中的数字块，可以上下左右移动，并且可以合并。
 */

public class Block {
    private Integer value; // 块的值
    private Integer[][] location; // 块的位置
    private Boolean isMerged; // 指示这个方块是否已经合并过
}
