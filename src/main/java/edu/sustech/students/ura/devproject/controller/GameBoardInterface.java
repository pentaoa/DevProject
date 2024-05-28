package edu.sustech.students.ura.devproject.controller;

public interface GameBoardInterface {
    void createTile(int row, int col, int value);
    void removeTile(int row, int col);
    void moveTile(int fromRow, int fromCol, int toRow, int toCol);
    void mergeTile(int fromRow, int fromCol, int toRow, int toCol);
    void setTileValue(int row, int col, int value);
    boolean haveTile (int row, int col);
}