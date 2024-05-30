package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameManager;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.Serial;
import java.io.Serializable;

public class GameBoard extends GridPane implements GameBoardInterface, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int SIZE = 4;
    private Tile[][] tiles = new Tile[SIZE][SIZE];

    public GameBoard() {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));
    }

    @Override
    public void createTile(int row, int col, int value) {
        tiles[row][col] = new Tile(value);
        add(tiles[row][col], col, row);
        SequentialTransition animation = tiles[row][col].createAnimation();
        animation.play();
    }

    @Override
    public void removeTile(int row, int col) {
        getChildren().remove(tiles[row][col]);
    }

    @Override
    public void moveTile(int fromRow, int fromCol, int toRow, int toCol) throws InterruptedException {
        // Create a translation animation
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), tiles[fromRow][fromCol]);
        transition.setByX((toCol - fromCol) * 75);
        transition.setByY((toRow - fromRow) * 75);
        transition.setOnFinished(event -> {
            tiles[fromRow][fromCol].setTranslateX(0);
            tiles[fromRow][fromCol].setTranslateY(0);
            getChildren().remove(tiles[fromRow][fromCol]);
            tiles[toRow][toCol].setValue(tiles[fromRow][fromCol].getValue());
            tiles[fromRow][fromCol].setValue(0);
            add(tiles[fromRow][fromCol], fromCol, fromRow);
        });
        transition.play();

        // Quick move
//        tiles[toRow][toCol].setValue(tiles[fromRow][fromCol].getValue());
//        tiles[fromRow][fromCol].setValue(0);
    }

    @Override
    public void mergeTile(int fromRow, int fromCol, int toRow, int toCol) {
        Tile fromTile = tiles[fromRow][fromCol];
        Tile toTile = tiles[toRow][toCol];

        // Quick Merge
//        toTile.setValue(toTile.getValue() * 2);
//        fromTile.setValue(0);

        // Animation Merge
        TranslateTransition transition = new TranslateTransition(Duration.millis(10), fromTile);
        transition.setByX((toCol - fromCol) * 75);
        transition.setByY((toRow - fromRow) * 75);
        transition.setOnFinished(event -> {
            toTile.setValue(toTile.getValue() * 2);
            fromTile.setValue(0);
            fromTile.setTranslateX(0);
            fromTile.setTranslateY(0);
            getChildren().remove(fromTile);
            add(fromTile, fromCol, fromRow);

            // Merge effect
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(10), toTile);
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);
            scaleTransition.play();
        });
        transition.play();
    }


    @Override
    public void setTileValue(int row, int col, int value) {
        tiles[row][col].setValue(value);
    }

    @Override
    public boolean haveTile(int row, int col) {
        return tiles[row][col] != null;
    }
}