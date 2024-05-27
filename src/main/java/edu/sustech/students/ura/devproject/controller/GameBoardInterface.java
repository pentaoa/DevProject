package edu.sustech.students.ura.devproject.controller;

public interface GameBoardInterface {
    void createTile(int row, int col, int value);
    void removeTile(int row, int col);
}