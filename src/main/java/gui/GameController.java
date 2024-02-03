package gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import core.Grid;
import core.Main;
import core.Moteur;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import lombok.Setter;
import org.w3c.dom.css.Rect;

import javax.swing.event.ChangeListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    @Setter
    private int gridSize = 150;
    private Rectangle[][] cells = new Rectangle[gridSize][gridSize];
    private Polygon[][] hexaCells = new Polygon[gridSize][gridSize];
    @Setter
    private static String[] colors;
    @Setter
    private static String[] alphabet;
    private String selectedColor;

    @FXML
    private Pane pane;

    @FXML
    private Button btn_update_once;

    @FXML
    private Button btn_play;

    @FXML
    private Button btn_pause;

    @FXML
    private Button btn_retour;

    @FXML
    private Button btn_save;

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

        Main.getMoteur().randomizeGrid();

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

        cmb_colors.getItems().addAll(Arrays.asList(this.alphabet));
        cmb_colors.getSelectionModel().selectFirst();
        EventHandler<ActionEvent> event_cmb =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        String selected = cmb_colors.getValue();
                        selectedColor = colors[Arrays.asList(alphabet).indexOf(selected)];
                    }
                };

        cmb_colors.setOnAction(event_cmb);
        if (Main.isHexa()) {
            initPaneHexa();
        } else {
            initPane();
        }

        btn_update_once.setOnAction(event -> {
            Main.getMoteur().update();
            if (Main.isHexa()) {
                displayPaneHexa();
            } else {
                displayPane();
            }
        });

        btn_play.setOnAction(event -> {
            this.play();
        });

        btn_pause.setOnAction(event -> {
            gameRunning.set(false);
            timeLine.pause();
        });

        btn_save.setOnAction(event -> {
            try {
                Main.getMoteur().saveGrid(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btn_retour.setOnAction(event -> {
            System.out.println("Retour au menu principal");
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/menu.fxml"));
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
        //Set up a new KeyFrame with he desired game speed interval.
        KeyFrame keyFrame = new KeyFrame(Duration.millis(gameSpeed), e -> {
            //This is the stuff that will be done each interval.
            //Generate the next game board state.
            Main.getMoteur().update();
            if (Main.isHexa()) {
                displayPaneHexa();
            } else {
                displayPane();
            }
            //Update the generation.
            //generation.set(generation.get() + 1);
        });
        //Attach the keyframe to the Timeline.
        timeLine = new Timeline(keyFrame);
        timeLine.setCycleCount(Timeline.INDEFINITE);
        //If the game was running before make it run again.
        if(gameRunning.get()) {
            timeLine.play();
        }
    }

    public void paramsFromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
            JsonArray colors = json.get("colors").getAsJsonArray();

            JsonArray alphabet = json.get("alphabet").getAsJsonArray();
            this.alphabet = alphabet.asList().stream().map(JsonElement::getAsString).toArray(String[]::new);

            if (alphabet.size() == 1){

                int colorNumber = Integer.parseInt(colors.get(0).getAsString());
                this.colors = new String[colorNumber];

                for (int i = 0; i < colorNumber; i++) {
                    //int hex = (int) (Math.random() * 256 * 256 * 256);
                    int r = (int) (colorNumber - i) * 255 / colorNumber;
                    int b = i * 255 / colorNumber;
                    int g = (int) (Math.random() * 255); // * colorNumber / colorNumber;
                    int hex = (r << 16) + (g << 8) + b;
                    String hexStr = Integer.toHexString(hex);
                    hexStr = "0".repeat(6 - hexStr.length()) + hexStr;
                    this.colors[i] = String.format("#" + hexStr);
                }
            } else
                this.colors = colors.asList().stream().map(JsonElement::getAsString).toArray(String[]::new);

        }
    }

    public void play() {
        gameRunning.set(true);
        timeLine.play();
    }
    public void initPane(){
        pane.getChildren().clear();

        int cellSize = 650/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Rectangle cellRect = new Rectangle();
                cellRect.setHeight(cellSize);
                cellRect.setWidth(cellSize);
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if (etat >= this.colors.length){
                    if (alphabet.length == 1){
                        cellRect.setFill(Color.web(this.colors[etat%colors.length]));
                    }
                    else
                        throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                else {
                    cellRect.setFill(Color.web(this.colors[etat]));
                }

                /*
                cellRect.setStroke(Color.web("#F6F6F6"));
                cellRect.setStrokeType(StrokeType.INSIDE);
                cellRect.setStrokeWidth(0.2);
                */
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

        int cellSize = 650/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if  (alphabet.length == 1){
                    etat = etat%colors.length;
                }
                if (etat >= this.colors.length) {
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                cells[i][j].setFill(Color.web(this.colors[etat]));
            }
        }
    }

    private void changeStateRectangle(javafx.scene.input.MouseEvent event){
        Rectangle eventSource = (Rectangle) event.getSource();
        double col = eventSource.getX()/(650/gridSize);
        double row = eventSource.getY()/(650/gridSize);
        int etat = Main.getMoteur().getEtat(new int[]{(int) col, (int) row});
        if (selectedColor != null){
            etat = Arrays.asList(this.colors).indexOf(selectedColor);
        }
        Main.getMoteur().setEtat(new int[]{(int) row, (int) col}, etat);
        displayPane();
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
                tile.getPoints().addAll(new Double[]{
                        0.0, 0.0 - cellSize/2*INVcos30,
                        0.0 - cellSize/2, 0.0-cellSize/4*INVcos30,
                        0.0 - cellSize/2, 0.0+cellSize/4*INVcos30,
                        0.0, 0.0 + cellSize/2*INVcos30,
                        0.0 + cellSize/2, 0.0+cellSize/4*INVcos30,
                        0.0 + cellSize/2, 0.0-cellSize/4*INVcos30
                });
                int etat = Main.getMoteur().getEtat(new int[]{j, i-j/2%gridSize});
                if (etat >= this.colors.length){
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                tile.setFill(Color.web(this.colors[etat]));
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
        int cellSize = 650/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = Main.getMoteur().getEtat(new int[]{i, j});
                if (etat >= this.colors.length){
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                hexaCells[i][j].setFill(Color.web(this.colors[etat]));
            }
        }
    }

    private void changeStatePolygon(javafx.scene.input.MouseEvent event){
        Polygon tile = (Polygon) event.getSource();
        int x = (int) (tile.getLayoutX()* gridSize/ 650);
        int y = (int) (tile.getLayoutY() * gridSize / (Math.sqrt(3)*325));
        int etat = Main.getMoteur().getEtat(new int[]{y, x-y/2%gridSize});
        if (selectedColor != null){
            etat = Arrays.asList(this.colors).indexOf(selectedColor);
        }
        Main.getMoteur().setEtat(new int[]{y, x}, etat);
        displayPaneHexa();
    }
}
