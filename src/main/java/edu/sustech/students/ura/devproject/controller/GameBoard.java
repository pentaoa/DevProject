package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameBoard extends GridPane implements GameBoardInterface {
    private static final int SIZE = 4;
    private StackPane[][] cells;
    private Tile[][] tiles;

    public GameBoard(GameManager gameManager) {
        cells = new StackPane[SIZE][SIZE];
        tiles = new Tile[SIZE][SIZE];
        initializeBoard(gameManager);
    }

    private void initializeBoard(GameManager gameManager) {
        String cellColor = "BDB3A9B2";

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                StackPane cell = new StackPane();
                Rectangle background = new Rectangle(75,75);
                background.setFill(Color.web(cellColor));// 设置 cell 颜色
                background.setStyle("-fx-arc-width: 15; -fx-arc-height: 15;");// 设置圆角
                cell.getChildren().add(background);
                cells[row][col] = cell;
                add(cell, col, row);
                Tile tile = new Tile(gameManager.getNumber(row, col).get());

                tiles[row][col] = tile;
                gameManager.getNumber(row,col).addListener((obs, oldVal, newVal) -> {
                    tile.setValue(newVal.intValue());
                });
                cell.getChildren().add(tile);
            }
        }
    }

    @Override
    public void createTile(int row, int col, int value) {
        tiles[row][col].setValue(value);
    }

    @Override
    public void removeTile(int row, int col) {
        tiles[row][col].setValue(0);
    }
}