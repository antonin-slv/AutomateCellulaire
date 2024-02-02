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
import org.w3c.dom.css.Rect;

import javax.swing.event.ChangeListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class GameController implements Initializable {

    Moteur moteur ;
    private int gridSize = 10;
    private Rectangle[][] cells = new Rectangle[gridSize][gridSize];
    private Polygon[][] hexaCells = new Polygon[gridSize][gridSize];
    private String[] colors;
    private String[] alphabet;
    private String selectedColor;

    @FXML
    private Pane pane;

    @FXML
    private Button btn_update_once;

    @FXML
    private Button btn_play;

    @FXML
    private Button btn_retour;

    @FXML
    private ComboBox<String> cmb_colors;

    public int gameSpeed = 100;

    private Timeline timeLine;
    private BooleanProperty gameRunning;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            URL rulesPath = Main.class.getClassLoader().getResource("rules/RealWoodFire.json");
            this.moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), gridSize);
            paramsFromJson(rulesPath.getPath());
            this.moteur.randomizeGrid();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

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
        initPaneHexa();
        //initPane();

        btn_update_once.setOnAction(event -> {
            this.moteur.update();
            displayPaneHexa();
        });

        btn_play.setOnAction(event -> {
            this.play();
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
            moteur.update();
            displayPaneHexa();
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
            this.colors = colors.asList().stream().map(JsonElement::getAsString).toArray(String[]::new);
            JsonArray alphabet = json.get("alphabet").getAsJsonArray();
            this.alphabet = alphabet.asList().stream().map(JsonElement::getAsString).toArray(String[]::new);
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
                int etat = this.moteur.getEtat(new int[]{i, j});
                if (etat >= this.colors.length){
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                cellRect.setFill(Color.web(this.colors[etat]));
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
                int etat = this.moteur.getEtat(new int[]{i, j});
                if (etat >= this.colors.length){
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
        int etat = this.moteur.getEtat(new int[]{(int) col, (int) row});
        if (selectedColor != null){
            etat = Arrays.asList(this.colors).indexOf(selectedColor);
        }
        this.moteur.setEtat(new int[]{(int) row, (int) col}, etat);
        pane.getChildren().clear();
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
                int etat = this.moteur.getEtat(new int[]{j, i-j/2%gridSize});
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
                int etat = this.moteur.getEtat(new int[]{i, j});
                if (etat >= this.colors.length){
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }
                hexaCells[i][j].setFill(Color.web(this.colors[etat]));
            }
        }
    }

    private void changeStatePolygon(javafx.scene.input.MouseEvent event){
        Polygon tile = (Polygon) event.getSource();
        int x = (int) (tile.getLayoutX() / (650/gridSize));
        int y = (int) (tile.getLayoutY() / (650/gridSize * (Math.sqrt(3)/2)));
        int etat = this.moteur.getEtat(new int[]{y, x-y/2%gridSize});
        if (selectedColor != null){
            etat = Arrays.asList(this.colors).indexOf(selectedColor);
        }
        this.moteur.setEtat(new int[]{y, x}, etat);
        displayPaneHexa();
    }
}
