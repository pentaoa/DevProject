package edu.sustech.students.ura.devproject.controller;

import edu.sustech.students.ura.devproject.model.GameStatus;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.Serial;
import java.io.Serializable;

public class Cell extends Rectangle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    GameStatus status = GameStatus.getInstance();

    public Cell() {
        setHeight(65);
        setWidth(65);
        setArcWidth(15);
        setArcHeight(15);
        setFill(Color.web("FFFAF54C"));
//        if (status.theme == 0)  setFill(Color.web("FFFAF584"));
//        if (status.theme == 1)  setFill(Color.web("92D7D9FF"));
//        if (status.theme == 2)  setFill(Color.web("FFFAF584"));
//        if (status.theme == 3)  setFill(Color.web("1F2A31FF"));
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
}
