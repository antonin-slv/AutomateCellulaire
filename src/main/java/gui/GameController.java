package gui;

import core.Grid;
import core.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @Setter
    private int gridSize;
    private int generation = 0;
    private Rectangle[][] cells;
    private Polygon[][] hexaCells;

    @Setter
    private static String[] colors;
    @Setter
    private static String[] alphabet;

    public static boolean is_ready() {
        return (colors != null && alphabet != null);
    }
    private String selectedColor;

    @FXML
    private Pane pane;

    @FXML
    private Label lbl_generation;

    @FXML
    private Button btn_update_once;

    @FXML
    private Button btn_play;

    @FXML
    private Button btn_pause;

    @FXML
    private Button btn_retour;

    @FXML
    private ComboBox<String> cmb_colors;

    @FXML
    private Slider speedSlider;

    public int gameSpeed = 100;

    private Timeline timeLine;
    private BooleanProperty gameRunning;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GameController initialize");

        this.gridSize = Main.getMoteur().getGrid().getSize();
        if (alphabet.length == 1) {
            Main.getMoteur().getGrid().setGridMaxEtat(Integer.parseInt(alphabet[0]));
            initializeColorsForContinue();
        }
        else {
            Main.getMoteur().getGrid().setGridMaxEtat(alphabet.length);
        }
        speedSlider.setMin(50);
        speedSlider.setMax(500);
        speedSlider.setValue(500 - gameSpeed + 50);

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            gameSpeed = (int) (speedSlider.getMax() - (int) newValue.doubleValue() + speedSlider.getMin());
            initializeTimeline(gameSpeed);
        });

        this.gameRunning = new SimpleBooleanProperty();
        this.gameRunning.set(false);
        initializeTimeline(gameSpeed);

        cmb_colors.getItems().addAll(Arrays.asList(alphabet));
        cmb_colors.getSelectionModel().selectFirst();
        EventHandler<ActionEvent> event_cmb =
                e -> {
                    String selected = cmb_colors.getValue();
                    selectedColor = colors[Arrays.asList(alphabet).indexOf(selected)];
                };

        cmb_colors.setOnAction(event_cmb);

        if (Main.isHexa()) {
            this.hexaCells = new Polygon[gridSize][gridSize];
            initPaneHexa();
        } else {
            this.cells = new Rectangle[gridSize][gridSize];
            if (Main.getDimension() == 1) {
                initPane1D();
            } else {
                initPane();
            }
        }

        btn_update_once.setOnAction(event -> {
            Main.getMoteur().update();
            generation++;
            lbl_generation.setText("Generation : " + generation);
            if (Main.isHexa()) {
                displayPaneHexa();
            } else {
                if (Main.getDimension() == 1) {
                    displayPane1D();
                } else {
                    displayPane();
                }
            }
        });

        btn_play.setOnAction(event ->
            this.play()
        );

        btn_pause.setOnAction(event -> {
            gameRunning.set(false);
            timeLine.pause();
        });

        btn_retour.setOnAction(event -> {
            System.out.println("Retour au menu principal");
            gameRunning.set(false);
            timeLine.pause();
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/menu.fxml")));
                Scene scene = new Scene(root);
                Main.getStage().setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    public void initializeTimeline(int gameSpeed) {

        this.gameSpeed = gameSpeed;
        //Stop any old timeline if it is still running.
        if(timeLine != null) {
            timeLine.stop();
        }
        //Set up a new KeyFrame with the desired game speed interval.
        KeyFrame keyFrame = new KeyFrame(Duration.millis(gameSpeed), e -> {
            //This is the stuff that will be done each interval.
            //Generate the next game board state.
            Main.getMoteur().update();
            generation++;
            lbl_generation.setText("Generation : " + generation);
            if (Main.isHexa()) {
                displayPaneHexa();
            } else {
                if (Main.getDimension() == 1) {
                    displayPane1D();
                } else {
                    displayPane();
                }
            }
        });
        //Attach the keyframe to the Timeline.
        timeLine = new Timeline(keyFrame);
        timeLine.setCycleCount(Timeline.INDEFINITE);
        //If the game was running before make it run again.
        if(gameRunning.get()) {
            timeLine.play();
        }
    }

    public void initializeColorsForContinue()
    {
            int colorNumber = Integer.parseInt(colors[0]);
            colors = new String[colorNumber];

            for (int i = 0; i < colorNumber; i++) {
                int r = (colorNumber - i) * 255 / colorNumber;
                int b = i * 255 / colorNumber;
                int g = (int) (Math.random() * 255); // * colorNumber / colorNumber;
                int hex = (r << 16) + (g << 8) + b;
                String hexStr = Integer.toHexString(hex);
                hexStr = "0".repeat(6 - hexStr.length()) + hexStr;
                colors[i] = String.format("#" + hexStr);
            }
    }


    public void play() {
        gameRunning.set(true);
        timeLine.play();
    }

    public void initPane1D(){
        pane.getChildren().clear();

        double cellSize = 650.0/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Rectangle cellRect = new Rectangle();
                cellRect.setHeight(cellSize);
                cellRect.setWidth(cellSize);
                int etat;
                if (i == 0) {
                    etat = Main.getMoteur().getEtat(new int[]{0, j});
                } else {
                    etat = 0;
                }
                if (etat >= colors.length || etat < 0){
                    if (alphabet.length == 1){
                        cellRect.setFill(Color.web(colors[etat%colors.length]));
                    }
                    else{
                        Main.getMoteur().setEtat(new int[]{i, j},0);
                        cellRect.setFill(Color.web(colors[0]));
                    }
                }
                else {
                    cellRect.setFill(Color.web(colors[etat]));
                }
                cellRect.setId(i+" "+j);
                cellRect.setSmooth(true);
                cellRect.setX(j*cellSize);
                cellRect.setY(i*cellSize);
                cellRect.setOnMouseClicked(this::changeStateRectangle);
                pane.getChildren().add(cellRect);
                this.cells[i][j] = cellRect;
            }
        }
    }

    private void displayPane1D(){
        for (int i = 0; i < gridSize; i++) {
            int etat = Main.getMoteur().getEtat(new int[]{i});
            if  (alphabet.length == 1){
                etat = etat%colors.length;
            }
            else if (etat >= colors.length) {
                Main.getMoteur().setEtat(new int[]{i},0);
                etat = 0;
            }
            cells[generation%gridSize][i].setFill(Color.web(colors[etat]));
        }
    }
    public void initPane(){
        pane.getChildren().clear();

        double cellSize = 650.0/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Rectangle cellRect = new Rectangle();
                cellRect.setHeight(cellSize);
                cellRect.setWidth(cellSize);
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if (etat >= colors.length || etat < 0){
                    if (alphabet.length == 1){
                        cellRect.setFill(Color.web(colors[etat%colors.length]));
                    }
                    else{
                        Main.getMoteur().setEtat(new int[]{i, j},0);
                        cellRect.setFill(Color.web(colors[0]));
                    }
                }
                else {
                    cellRect.setFill(Color.web(colors[etat]));
                }
                cellRect.setId(i+" "+j);
                cellRect.setSmooth(true);
                cellRect.setX(j*cellSize);
                cellRect.setY(i*cellSize);
                cellRect.setOnMouseClicked(this::changeStateRectangle);
                pane.getChildren().add(cellRect);
                this.cells[i][j] = cellRect;
            }
        }
    }
    private void displayPane(){
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if  (alphabet.length == 1){
                    etat = etat%colors.length;
                }
                else if (etat >= colors.length) {
                    Main.getMoteur().setEtat(new int[]{i, j},0);
                    etat = 0;
                }
                cells[i][j].setFill(Color.web(colors[etat]));
            }
        }
    }

    private void changeStateRectangle(javafx.scene.input.MouseEvent event){
        Rectangle eventSource = (Rectangle) event.getSource();
        String[] id = eventSource.getId().split(" ");
        int col =  Integer.parseInt(id[1]);
        int row =  Integer.parseInt(id[0]);
        int etat = Main.getMoteur().getEtat(new int[]{col, row});
        if (selectedColor != null){
            etat = Arrays.asList(colors).indexOf(selectedColor);
            eventSource.setFill(Color.web(selectedColor));
        }
        Main.getMoteur().setEtat(new int[]{row, col}, etat);
    }
    private void initPaneHexa(){
        double cos30 = Math.sqrt(3)/2;
        double INVcos30 = 1/cos30;
        double cellSize = 650.0/gridSize;
        double dy = cellSize*(1-cos30);


        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Polygon tile = new Polygon();
                if (j%2 == 1) {
                    tile.setLayoutX(i*cellSize+cellSize/2);
                } else {
                    tile.setLayoutX(i*cellSize);
                }
                tile.setLayoutY(j*(cellSize-dy)+cellSize/2);
                tile.getPoints().addAll(0.0, 0.0 - cellSize/2*INVcos30,
                        0.0 - cellSize/2, 0.0-cellSize/4*INVcos30,
                        0.0 - cellSize/2, 0.0+cellSize/4*INVcos30,
                        0.0, 0.0 + cellSize/2*INVcos30,
                        0.0 + cellSize/2, 0.0+cellSize/4*INVcos30,
                        0.0 + cellSize/2, 0.0-cellSize/4*INVcos30);
                int etat = Main.getMoteur().getEtat(new int[]{j, i-j/2%gridSize});
                if (etat >= colors.length){
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                tile.setId(j+" "+i);
                tile.setFill(Color.web(colors[etat]));
                tile.setStroke(Color.web("#F6F6F6"));
                tile.setStrokeType(StrokeType.INSIDE);
                tile.setStrokeWidth(0.2);
                tile.setSmooth(true);
                tile.setOnMouseClicked(this::changeStatePolygon);
                pane.getChildren().add(tile);
                this.hexaCells[j][i] = tile;
            }
        }
    }
    private void displayPaneHexa(){
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if  (alphabet.length == 1){
                    etat = etat%colors.length;
                }
                else if (etat >= colors.length) {
                    Main.getMoteur().setEtat(new int[]{i, j},0);
                    etat = 0;
                }
                hexaCells[i][j].setFill(Color.web(colors[etat]));
            }
        }
    }

    private void changeStatePolygon(javafx.scene.input.MouseEvent event){
        Polygon tile = (Polygon) event.getSource();
        String[] id = tile.getId().split(" ");
        int x = Integer.parseInt(id[1]);
        int y = Integer.parseInt(id[0]);
        int etat = Main.getMoteur().getEtat(new int[]{y,(x-y/2%gridSize)});
        if (selectedColor != null){
            etat = Arrays.asList(colors).indexOf(selectedColor);
            tile.setFill(Color.web(selectedColor));
        }
        Main.getMoteur().setEtat(new int[]{y, x}, etat);
    }
}
