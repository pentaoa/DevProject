package edu.sustech.students.ura.devproject.controller;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

/**
 *  Tile
 *  砖块
 */
public class Tile extends StackPane {
    private int value;
    private Rectangle tile;
    private Text text;

    public Tile(int value) {
        this.value = value;
        tile = new Rectangle(100, 100);
        tile.setFill(getColorByValue(value));
        text = new Text(String.valueOf(value));
        text.setFont(Font.font(24));
        text.setFill(Color.BLACK);

        getChildren().addAll(tile, text);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        text.setText(String.valueOf(value));
        tile.setFill(getColorByValue(value));
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 2: return Color.LIGHTGRAY;
            case 4: return Color.LIGHTYELLOW;
            case 8: return Color.LIGHTCORAL;
            // Add more cases for higher values with different colors
            default: return Color.WHITE;
        }
    }
}