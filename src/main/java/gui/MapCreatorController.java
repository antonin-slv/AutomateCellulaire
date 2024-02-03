package gui;

import core.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MapCreatorController implements Initializable {

    Grid grid;
    private final List<String> colors = Arrays.asList("#FFFFFF", "#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF");

    private Rectangle[][] cells;
    private Polygon[][] hexaCells;

    @FXML
    private Pane pane;
    @FXML
    private Button btn_load;
    @FXML
    private ComboBox<String> cb_load;
    @FXML
    private Button btn_save;
    @FXML
    private TextField fild_save;
    @FXML
    private TextField fild_size;
    @FXML
    private Button btn_new;
    @FXML
    private ComboBox<String> cb_colors;
    @FXML
    private ComboBox<String> cb_pen;
    @FXML
    private TextField fild_replace_1;
    @FXML
    private TextField fild_replace_2;
    @FXML
    private Button btn_replace;
    @FXML
    private Button btn_random;
    @FXML
    private TextField fild_random;
    @FXML
    private Button btn_back;


    public void initialize(URL url, ResourceBundle rb) {

        this.grid = Main.getMoteur().getGrid();

        cb_load.getItems().addAll(getMaps());

        cb_colors.getItems().addAll(colors);
        cb_colors.getSelectionModel().select(0);
        fild_size.setText(String.valueOf(this.grid.getSize()));

        fild_replace_1.setText("0");
        fild_replace_2.setText("0");
        fild_random.setText("2");

        btn_new.setOnAction(event -> {
                    this.grid = new Grid(2, Integer.parseInt(fild_size.getText()));
                    if (Main.isHexa()) {
                        initPaneHexa();
                    } else {
                        initPane();
                    }

                    display();
                }
        );

        btn_replace.setOnAction(event -> {
                    int etat1 = Integer.parseInt(fild_replace_1.getText());
                    int etat2 = Integer.parseInt(fild_replace_2.getText());
                    this.grid.replace(etat1, etat2);
                    display();
                }
        );

        btn_back.setOnAction(event -> {
                    try {
                        Main.getMoteur().setGrid(this.grid);
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/menu.fxml")));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_random.setOnAction(event -> {
                    this.grid.randomize(Integer.parseInt(fild_random.getText()));
                    display();
                    }
        );

        btn_load.setOnAction(event -> {
                    try {
                        this.grid = Grid.fromJson("save/" + cb_load.getValue());
                        if (Main.isHexa()) {
                            initPaneHexa();
                        } else {
                            initPane();
                        }

                        display();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        btn_save.setOnAction(event -> {
                    try {
                        this.grid.toJson(fild_save.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        if (Main.isHexa()) {
            initPaneHexa();
        } else {
            initPane();
        }

        display();
    }

    public void display(){
        if (Main.isHexa()) {
            displayPaneHexa();
        } else {
            displayPane();
        }
    }
    public void initPane(){

        this.cells = new Rectangle[this.grid.getSize()][this.grid.getSize()];
        this.pane.getChildren().clear();
        int gridSize = this.grid.getSize();

        int cellSize = 650/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Rectangle cellRect = new Rectangle();
                cellRect.setHeight(cellSize);
                cellRect.setWidth(cellSize);
                int stat = this.grid.getCase(new int[]{i, j});

                if (stat >= this.colors.size())
                    cellRect.setFill(Color.web("#F0F0F0"));
                else
                    cellRect.setFill(Color.web(this.colors.get(stat)));

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
        int gridSize = this.grid.getSize();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = this.grid.getCase(new int[]{i, j});
                if (etat >= this.colors.size())
                    cells[i][j].setFill(Color.web("#F0F0F0"));
                else
                    cells[i][j].setFill(Color.web(this.colors.get(etat)));
            }
        }
    }
    private void changeStateRectangle(javafx.scene.input.MouseEvent event){
        int gridSize = this.grid.getSize();
        Rectangle eventSource = (Rectangle) event.getSource();
        double col = eventSource.getX()/(650/gridSize);
        double row = eventSource.getY()/(650/gridSize);
        int etat = this.colors.indexOf(cb_colors.getValue());
        this.grid.setCase(new int[]{(int) row, (int) col}, etat);
        displayPane();
    }
    private void initPaneHexa(){
        this.hexaCells = new Polygon[this.grid.getSize()][this.grid.getSize()];
        int gridSize = this.grid.getSize();
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
                int etat = this.grid.getCase(new int[]{j, i-j/2%gridSize});
                if (etat >= this.colors.size())
                    tile.setFill(Color.web("#F0F0F0"));
                else
                    tile.setFill(Color.web(this.colors.get(etat)));

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
        int gridSize = this.grid.getSize();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int etat = this.grid.getCase(new int[]{i, j});
                if (etat >= this.colors.size())
                    hexaCells[i][j].setFill(Color.web("#F0F0F0"));
                else
                    hexaCells[i][j].setFill(Color.web(this.colors.get(etat)));
            }
        }
    }
    private void changeStatePolygon(javafx.scene.input.MouseEvent event){
        int gridSize = this.grid.getSize();
        Polygon tile = (Polygon) event.getSource();
        int x = (int) (tile.getLayoutX()* gridSize/ 650);
        int y = (int) (tile.getLayoutY() * gridSize / (Math.sqrt(3)*325));
        int etat = this.colors.indexOf(cb_colors.getValue());

        this.grid.setCase(new int[]{y, x}, etat);
        displayPaneHexa();
    }

    private String[] getMaps() {
        File folder = new File("save");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        String[] save = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) save[i] = listOfFiles[i].getName();
        }
        return save;
    }
}