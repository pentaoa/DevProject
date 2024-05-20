package edu.sustech.students.ura.devproject.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

/**
 *  Tile
 *  砖块
 *  @version 1.0
 */
public class Tile extends StackPane {
    private IntegerProperty value;
    private Rectangle tile;
    private Text text;

    public Tile(int initialValue) {
        value = new SimpleIntegerProperty(initialValue);
        tile = new Rectangle(100, 100);
        tile.setFill(getColorByValue(initialValue));
        text = new Text(String.valueOf(initialValue));
        text.setFont(Font.font(24));
        text.setFill(Color.BLACK);

        getChildren().addAll(tile, text);

        // 监听值的变化
        value.addListener((observable, oldValue, newValue) -> {
            tile.setFill(getColorByValue(newValue.intValue()));
            text.setText(String.valueOf(newValue.intValue()));
        });
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    private Color getColorByValue(int value) {
        return switch (value) {
            case 2 -> Color.LIGHTGRAY;
            case 4 -> Color.LIGHTYELLOW;
            case 8 -> Color.LIGHTCORAL;
            // Add more cases for higher values with different colors
            default -> Color.WHITE;
        };
    }
}