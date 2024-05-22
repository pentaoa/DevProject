package edu.sustech.students.ura.devproject.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

import java.util.Objects;

public class Tile extends StackPane {
    private IntegerProperty value;
    private Rectangle tile;
    private Text text;

    public Tile(int initialValue) {
        value = new SimpleIntegerProperty(initialValue);
        // 简单地设置Tile的样式
        tile = new Rectangle(65, 65);
        tile.setArcWidth(15);
        tile.setArcHeight(15);
        text = new Text(String.valueOf(initialValue));

        getChildren().addAll(tile, text);

        // 在Tile值变化的时候，更新Tile的样式
        value.addListener((observable, oldValue, newValue) -> {
            updateStyle(newValue.intValue());
            text.setText(String.valueOf(newValue.intValue()));
        });

        // 初始化Tile的样式
        updateStyle(initialValue);
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    private void updateStyle(int value) {
        String backgroundColor;
        String textColor;
        int fontSize;

        switch (value) {
            case 0:
                backgroundColor = "FFFAF584";
                textColor = "776E6500";
                fontSize = 40;
                break;
            case 2:
                backgroundColor = "#eee4da";
                textColor = "#776e65";
                fontSize = 40;
                break;
            case 4:
                backgroundColor = "#ede0c8";
                textColor = "#776e65";
                fontSize = 40;
                break;
            case 8:
                backgroundColor = "#f2b179";
                textColor = "#f9f6f2";
                fontSize = 40;
                break;
            case 16:
                backgroundColor = "#f59563";
                textColor = "#f9f6f2";
                fontSize = 40;
                break;
            case 32:
                backgroundColor = "#f67c5f";
                textColor = "#f9f6f2";
                fontSize = 40;
                break;
            case 64:
                backgroundColor = "#f65e3b";
                textColor = "#f9f6f2";
                fontSize = 40;
                break;
            case 128:
                backgroundColor = "#edcf72";
                textColor = "#f9f6f2";
                fontSize = 30;
                break;
            case 256:
                backgroundColor = "#edcc61";
                textColor = "#f9f6f2";
                fontSize = 30;
                break;
            case 512:
                backgroundColor = "#edc850";
                textColor = "#f9f6f2";
                fontSize = 30;
                break;
            case 1024:
                backgroundColor = "#edc53f";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            case 2048:
                backgroundColor = "#edc22e";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            case 4096:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            case 8192:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            case 16384:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            case 32768:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize = 20;
                break;
            default:
                backgroundColor = "#FFFFFF";
                textColor = "#000000";
                fontSize = 40;
                break;
        }
        Font font = Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/HYWenHei-85W.ttf")).toExternalForm(), fontSize);
        tile.setFill(Color.web(backgroundColor));
        text.setFill(Color.web(textColor));
        text.setFont(font);
    }
}