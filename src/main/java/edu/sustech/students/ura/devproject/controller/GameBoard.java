package edu.sustech.students.ura.devproject.controller;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoard extends GridPane {
    private static final int SIZE = 4;
    private StackPane[][] cells;

    public GameBoard() {
        cells = new StackPane[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = new StackPane();
                Rectangle background = new Rectangle(100, 100);
                background.setFill(Color.BEIGE);
                cell.getChildren().add(background);
                cells[row][col] = cell;
                add(cell, col, row);
            }
        }
    }

    public void addTile(Tile tile, int row, int col) {
        cells[row][col].getChildren().add(tile);
    }

    public void moveTile(Tile tile, int newRow, int newCol) {
        // Find the current position of the tile
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getChildren().contains(tile)) {
                    cells[row][col].getChildren().remove(tile);
                    cells[newRow][newCol].getChildren().add(tile);
                    return;
                }
            }
        }
    }
}