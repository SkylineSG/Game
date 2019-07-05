package com.kodilla;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.awt.*;

import static javafx.application.Application.launch;

public class Test extends Application {
    Button button;
    private Image imageback = new Image("file/milky-way.jpg");


    private Parent createContent() {
        button = new Button();
        button.setText("Statistics");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Pane root = new Pane();
        root.setPrefSize(600, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);
            }
        }
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        BackgroundSize backgroundSize = new BackgroundSize(50, 50, true, true, true, false);
//        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//        Background background = new Background(backgroundImage);
//
//        GridPane grid = new GridPane();
//        grid.setAlignment(Pos.CENTER);
//        grid.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
//        grid.setHgap(5.5);
//        grid.setVgap(5.5);
//        grid.setBackground(background);
//
//        Scene scene = new Scene(grid, 700, 500,Color.DARKSLATEBLUE);


        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("TicTacToe");

        primaryStage.show();
    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile() {
            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    drawX();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    drawO();
                }
            });
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

