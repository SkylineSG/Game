package com.kodilla;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

import static javafx.application.Application.launch;

public class Test extends Application {
    Stage window;
    Scene scene1, scene2;

    private boolean playable = true;
    MenuBar bar = new MenuBar();
    boolean isComputerMove = false;
    private Image imageback = new Image("file/background-600x600.jpg");
    private Image imageback2 = new Image("file/dark-background-300x200.jpg");
    private Tile[][] board = new Tile[3][3];
    private List<Combo> combos = new ArrayList<>();
    TextField fieldName = new TextField();
    TextField fieldHP = new TextField();

    private Pane root = new Pane();

    private Parent createContent() {
        BackgroundSize backgroundSize = new BackgroundSize(600, 600, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);


        Menu menu = new Menu("Settings");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> {
            System.out.println( "Restarting app!" );
            window.close();
            Platform.runLater( () -> {
                try {
                    new Test().start( new Stage() );
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> {
            Save data = new Save();
            data.name = fieldName.getText();
         //   data.hp = Integer.parseInt(fieldHP.getText());
            try {
                ResourceManager.save(data, "1.save");
            }
            catch (Exception e) {
                System.out.println("Couldn't save: " + e.getMessage());
            }
        });



        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> System.exit(0));

        menu.getItems().add(save);
        menu.getItems().add(newGame);
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(exit);
        bar.getMenus().add(menu);


        StackPane layout = new StackPane();


        root.setPrefSize(600, 600);
        root.setBackground(background);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);

                board[j][i] = tile;
            }
        }
        root.getChildren().addAll(bar);

        // horizontal
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        // vertical
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("TicTacToe");
        BackgroundSize backgroundSize = new BackgroundSize(300, 200, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback2, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        Button btnLoad = new Button("LOAD");
        btnLoad.setOnAction(event -> {
            try {
                Save data = (Save) ResourceManager.load("1.save");
                fieldName.setText(data.name);
              //  fieldHP.setText(String.valueOf(data.hp));
            }
            catch (Exception e) {
                System.out.println("Couldn't load save data: " + e.getMessage());
            }
        });



//        ToggleGroup selectGame = new ToggleGroup();
//        RadioButton rd1 = new RadioButton("player vs player");
//        rd1.setStyle("-fx-text-fill: aliceblue ");
//        RadioButton rd2 = new RadioButton("player vs computer");
//        rd2.setStyle("-fx-text-fill: aliceblue ");
//        rd1.setToggleGroup(selectGame);
//        rd2.setToggleGroup(selectGame);

        Button button1 = new Button("Start");
        button1.setStyle("-fx-text-fill: green");
        button1.setFont(Font.font(30));
        button1.setOnAction(e -> window.setScene(new Scene(createContent())));

//        Button button2 = new Button("Exit");
//        button2.setStyle("-fx-text-fill: red");
//        button2.setFont(Font.font(30));
//        button2.setOnAction(e -> System.exit(0));

//        userInput.setTranslateX(55);
//        userInput.setTranslateY(-70);
//        user.setTranslateX(-168);
//        user.setTranslateY(-20);
//        rd1.setTranslateX(0);
//        rd1.setTranslateY(30);
//
//        rd2.setTranslateX(120);
//        rd2.setTranslateY(30);
     //   button1.setTranslateX(00);
       // button1.setTranslateY(100);
//        button2.setTranslateY(100);
//        button2.setTranslateX(110);
//        grid.getChildren().addAll(button1, button2, user, userInput,rd1,rd2,btnLoad);
        VBox vbox = new VBox(10, fieldName,   btnLoad,button1);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(50, 50, 50, 50));




        Scene scene = new Scene(vbox, 300, 200);
//        grid.setBackground(background);
        window.setScene(scene);
        window.show();
    }


    public void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                winAnimation(combo);
                break;
            }
        }
    }

    private void winAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }

    public class Combo {
        private Tile[] tiles;

        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }


        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private class Tile extends StackPane {
        private Text text = new Text();
        boolean check = false;

        public Tile() {
            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (!playable) {
                    return;
                }

                if (isComputerMove) {
                    Random random = new Random();
                    boolean isEmptyTile = false;
                    do {

                        int column = random.nextInt(3);
                        int row = random.nextInt(3);

                        if (!board[column][row].check) {
                            board[column][row].drawX();
                            isEmptyTile = true;
                        }
                    } while (!isEmptyTile);

                    checkState();
                    isComputerMove = false;
                } else {
                    drawO();
                    checkState();
                    isComputerMove = true;
                }
                check = true;
            });
        }

        public double getCenterX() {
            return getTranslateX() + 100;
        }

        public double getCenterY() {
            return getTranslateY() + 100;
        }

        public String getValue() {
            return text.getText();
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

