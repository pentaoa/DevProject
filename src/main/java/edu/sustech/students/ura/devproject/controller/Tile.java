package edu.sustech.students.ura.devproject.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Tile extends StackPane implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int value;
    private Rectangle tile;
    private Text text;

    public Tile(int initialValue) {
        value = initialValue;
        // 简单地设置Tile的样式
        tile = new Rectangle(65, 65);
        tile.setArcWidth(15);
        tile.setArcHeight(15);
        text = new Text(String.valueOf(initialValue));

        getChildren().addAll(tile, text);

        // 初始化Tile的样式
        update(initialValue);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value=value;
        update(value);
    }

    public SequentialTransition createAnimation() {
        ScaleTransition firstAnimation = new ScaleTransition(Duration.seconds(0.1), this);
        firstAnimation.setFromX(0.8);
        firstAnimation.setFromY(0.8);
        firstAnimation.setToX(1.1);
        firstAnimation.setToY(1.1);

        ScaleTransition secondAnimation = new ScaleTransition(Duration.seconds(0.1), this);
        secondAnimation.setFromX(1.1);
        secondAnimation.setFromY(1.1);
        secondAnimation.setToX(1.0);
        secondAnimation.setToY(1.0);

        SequentialTransition combinedAnimation = new SequentialTransition(firstAnimation, secondAnimation);
        return combinedAnimation;
    }

    public SequentialTransition moveAnimation(int fromRow, int fromCol, int toRow, int toCol) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.1), this);
        move.setByX((toCol - fromCol) * 75);
        move.setByY((toRow - fromRow) * 75);
        return new SequentialTransition(move);
    }

    private void update(int value) {
        String backgroundColor;
        String textColor;
        int fontSize;
        getChildren().remove(text);
        text = new Text(String.valueOf(value));
        getChildren().add(text);
        switch (value) {
            case -1:
                backgroundColor = "D4DEE7D5";
                textColor = "776E6500";
                fontSize = 40;
                break;
            case 0:
                backgroundColor = "00000000";
                textColor = "00000000";
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