package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameStatus;
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
    GameStatus status = GameStatus.getInstance();

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
                if (status.theme==0) {
                    backgroundColor = "D4DEE7D5";
                    textColor = "776E6500";
                }
                else if (status.theme==1){
                    backgroundColor = "#afc8dcd6";
                    textColor = "776E6500";
                }else if (status.theme == 2){
                    backgroundColor = "#bad3e7d6";
                    textColor = "776E6500";
                }else
                {
                    backgroundColor = "#1b1f25d6";
                    textColor = "776E6500";
                }
                fontSize = 40;
                break;
            case 0:
                backgroundColor = "00000000";
                textColor = "00000000";
                fontSize = 40;
                break;
            case 2:
                if (status.theme==0) {
                    backgroundColor = "#eee4da";
                    textColor = "2C2724FF";
                }
                else if (status.theme==1){
                    backgroundColor = "#a3c485";
                    textColor = "F1ECE4FF";
                }else if (status.theme == 2){
                    backgroundColor = "#e5d9ca";
                    textColor = "2C2724FF";
                }
                else {
                    backgroundColor = "#2c251e";
                    textColor = "D5CAC4FF";
                }
                fontSize = 40;
                break;
            case 4:
                if (status.theme==0) {
                    backgroundColor = "#ede0c8";
                    textColor = "#776e65";
                }
                else if (status.theme==1){
                    backgroundColor = "#6cb99c";
                    textColor = "#777365";
                }else if (status.theme == 2){
                    backgroundColor = "#cfe5dc";
                    textColor = "#31312d";
                }else {
                    backgroundColor = "#1b2623";
                    textColor = "#b1eed1";
                }
                fontSize = 40;
                break;
            case 8:
                if (status.theme==0) {
                    backgroundColor = "#f2b179";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#418157";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#d9bdbf";
                    textColor = "#42382e";
                }else {
                    backgroundColor = "#362125";
                    textColor = "#ffcea6";
                }
                fontSize = 40;
                break;
            case 16:
                if (status.theme==0) {
                    backgroundColor = "#f59563";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#19703b";
                    textColor = "#a8d2af";
                }else if (status.theme == 2){
                    backgroundColor = "#c1e2e3";
                    textColor = "#2d3d2f";
                }else {
                    backgroundColor = "#26393a";
                    textColor = "#b4e7bb";
                }
                fontSize = 40;
                break;
            case 32:
                if (status.theme==0) {
                    backgroundColor = "#f67c5f";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#426e91";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#c3d6e7";
                    textColor = "#f9f6f2";
                }else {
                    backgroundColor = "#27313b";
                    textColor = "#f9f6f2";
                }
                fontSize = 40;
                break;
            case 64:
                if (status.theme==0) {
                    backgroundColor = "#f65e3b";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#3ba8f6";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#333e46";
                    textColor = "#f9f6f2";
                }else {
                    backgroundColor = "#333e46";
                    textColor = "#f9f6f2";
                }
                fontSize = 40;
                break;
            case 128:

                if (status.theme==0) {
                    backgroundColor = "#edcf72";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#94c07a";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#c4d9b8";
                    textColor = "#442d0e";
                }else {
                    backgroundColor = "#263120";
                    textColor = "#ece4db";
                }
                fontSize = 30;
                break;
            case 256:

                if (status.theme==0) {
                    backgroundColor = "#edcc61";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#384939";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#a59bbe";
                    textColor = "#23233a";
                }else {
                    backgroundColor = "#282333";
                    textColor = "#c3c3f5";
                }
                fontSize = 30;
                break;
            case 512:

                if (status.theme==0) {
                    backgroundColor = "#edc850";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#bfa6e5";
                    textColor = "#f9f6f2";
                }else if (status.theme == 2){
                    backgroundColor = "#ccbbe0";
                    textColor = "#211e19";
                }else {
                    backgroundColor = "#304846";
                    textColor = "#eeeded";
                }
                fontSize = 30;
                break;
            case 1024:

                if (status.theme==0) {
                    backgroundColor = "#edc53f";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#7dc78b";
                    textColor = "#2f3a2e";
                }else if (status.theme == 2){
                    backgroundColor = "#e7d3c3";
                    textColor = "#1b211b";
                }else {
                    backgroundColor = "#342e23";
                    textColor = "#d2e3d2";
                }
                fontSize = 20;
                break;
            case 2048:

                if (status.theme==0) {
                    backgroundColor = "#edc22e";
                    textColor = "#f9f6f2";
                }
                else if (status.theme==1){
                    backgroundColor = "#9ba14d";
                    textColor = "#c4b9ab";
                }else if (status.theme == 2){
                    backgroundColor = "#d9d9a4";
                    textColor = "#3a2d21";
                }else {
                    backgroundColor = "#464630";
                    textColor = "#efe5dc";
                }
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
                fontSize = 10;
                break;
            case 32768:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize = 10;
                break;
            case 65536:
                backgroundColor = "#3c3a32";
                textColor = "#f9f6f2";
                fontSize =10;
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

