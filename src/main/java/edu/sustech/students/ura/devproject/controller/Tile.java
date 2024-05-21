package edu.sustech.students.ura.devproject.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;

public class Tile extends StackPane {
    private IntegerProperty value;
    private Rectangle tile;
    private Text text;

    public Tile (int initialValue) {
        value = new SimpleIntegerProperty(initialValue);
        tile = new Rectangle(65, 65);
        // Initially set the CSS class based on the initial value
        tile.getStyleClass().add("game-tile-" + initialValue);
        text = new Text(String.valueOf(initialValue));

        getChildren().addAll(tile, text);

        // Update the CSS class whenever the value changes
        value.addListener((observable, oldValue, newValue) -> {
            // Remove the old style class
            tile.getStyleClass().remove("game-tile-" + oldValue.intValue());
            // Add the new style class
            tile.getStyleClass().add("game-tile-" + newValue.intValue());
            text.setText(String.valueOf(newValue.intValue()));
        });
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        this.value.set(value);
    }
}