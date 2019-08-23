package main.controller;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TextRectangle extends Rectangle {

    private Text text;

    public TextRectangle(double width, double height, Text text) {
        super(width, height);
        this.text = text;
    }

    public void displayText(double X, double Y) {
        text.setX(X);
        text.setY(Y);
        text.setStyle("-fx-text-fill: black");
        text.setVisible(true);
    }


}
