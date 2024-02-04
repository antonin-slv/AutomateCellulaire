package gui;

import core.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

/**
 * Controller of the map creator.
 * It allows to create a grid and to save it.
 * It also allows to load a grid and to modify it.
 * @see core.Grid
 * @see Initializable
 */
public class MapCreatorController implements Initializable {

    /** The grid MapCreatorController use to work with
     *  ths grid only have integers. Colors and alphabet are not a story for this Module.
     * */
    Grid grid;
    /** List of 8 colors the MapCreator use for visualisation purpose */
    private final List<String> colors = Arrays.asList("#FFFFFF", "#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF");
    /** Array of the cells of the grid in case of non-Hexagonal grid */
    private Rectangle[][] cells;
    /** Array of the cells of the grid if it is a Hexagonal one*/
    private Polygon[][] hexCells;

    /** Pane where the grid is displayed */
    @FXML
    private Pane pane;
    /** Button to load a grid */
    @FXML
    private Button btn_load;
    /** ComboBox to select the grid to load */
    @FXML
    private ComboBox<String> cb_load;
    /** Button to save the grid */
    @FXML
    private Button btn_save;
    /** TextField to write the name of the grid to save */
    @FXML
    private TextField fild_save;
    /** TextField to write the size of the grid */
    @FXML
    private TextField fild_size;
    /** Button to change grid size*/
    @FXML
    private Button btn_new;
    /** ComboBox to select the color (and int) to draw */
    @FXML
    private ComboBox<String> cb_colors;
    /** ComboBox to select the pen size */
    @FXML
    private ComboBox<String> cb_pen;

    /** TextField to write the state to be replaced */
    @FXML
    private Label lbl_pen;
    @FXML
    private TextField fild_replace_1;
    /** TextField to write the state to replace with */
    @FXML
    private TextField fild_replace_2;
    /** Button to replace a state with another */
    @FXML
    private Button btn_replace;
    /** Button to randomize the grid */
    @FXML
    private Button btn_random;
    /** TextField to write the number of states */
    @FXML
    private TextField fild_random;
    /** Button to go back to the menu */
    @FXML
    private Button btn_back;


    /**
     * Initialize the MapCreatorController.
     * It sets the grid, ComboBoxes, TextFields and Buttons functions
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle rb) {

        this.grid = Main.getMoteur().getGrid();

        cb_load.getItems().addAll(getMaps());
        cb_pen.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20");
        cb_pen.getSelectionModel().select(0);
        cb_pen.setVisible(!Main.isHexa());
        lbl_pen.setVisible(!Main.isHexa());

        cb_colors.getItems().addAll(colors);
        cb_colors.getSelectionModel().select(0);
        fild_size.setText(String.valueOf(this.grid.getSize()));

        fild_replace_1.setText("0");
        fild_replace_2.setText("0");
        fild_random.setText("2");

        btn_new.setOnAction(event -> {
                    if (Main.getDimension() == 1) {
                        this.grid = new Grid(1, Integer.parseInt(fild_size.getText()));
                    } else {
                        this.grid = new Grid(2, Integer.parseInt(fild_size.getText()));
                    }
                    if (Main.isHexa()) {
                        initPaneHexa();
                    } else {
                        if (Main.getDimension() == 1) {
                            initPane1D();
                        } else {
                            initPane();
                        }
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
                    if(cb_load.getValue() == null)
                        return;
                    try {
                        this.grid = Grid.fromJson("save/" + cb_load.getValue());
                        if (Main.getDimension() == 1) {
                            int[] newGrid = new int[this.grid.getSize()];
                            for (int i = 0; i < this.grid.getSize(); i++) {
                                newGrid[i] = this.grid.getCase(new int[]{0, i});
                            }
                            this.grid = new Grid(1, this.grid.getSize(), newGrid);
                        }
                        if (Main.isHexa()) {
                            initPaneHexa();
                        } else {
                            if (Main.getDimension() == 1) {
                                initPane1D();
                            } else {
                                initPane();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        btn_save.setOnAction(event -> {
                    if(fild_save.getText() == null)
                        return;
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
            if (Main.getDimension() == 1) {
                initPane1D();
            } else {
                initPane();
            }
        }

        display();
    }

    /**
     * Display the grid.
     * It uses the displayPane() or displayPaneHexa() method to display the grid.
     */
    public void display(){
        if (Main.isHexa()) {
            displayPaneHexa();
        } else {
            if (Main.getDimension() == 1) {
                displayPane1D();
            } else {
                displayPane();
            }
        }
    }
    /**
     * Initialize the grid in the pane.
     * It uses the cells array to display the grid.
     */
    public void initPane(){
        pane.getChildren().clear();
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


/**
     * Initialize the grid in the pane for 1D grids.
     * It uses the cells array to display the grid.
     */
    public void initPane1D(){
        pane.getChildren().clear();
        this.cells = new Rectangle[this.grid.getSize()][this.grid.getSize()];
        this.pane.getChildren().clear();
        int gridSize = this.grid.getSize();

        int cellSize = 650/gridSize;

        for (int i = 0; i < gridSize; i++) {
            Rectangle cellRect = new Rectangle();
            cellRect.setHeight(cellSize);
            cellRect.setWidth(cellSize);
            int stat = this.grid.getCase(new int[]{0, i});

            if (stat >= this.colors.size())
                cellRect.setFill(Color.web("#F0F0F0"));
            else
                cellRect.setFill(Color.web(this.colors.get(stat)));

            cellRect.setSmooth(true);
            cellRect.setX(i * cellSize);
            cellRect.setY(0);
            cellRect.setOnMouseClicked(this::changeStateRectangle);
            pane.getChildren().add(cellRect);
            this.cells[i][0] = cellRect;
        }
    }
    /**
     * Update colors of the cells in the grid for 2D non-Hexagonal grids.
     */
    private void displayPane(){
        int gridSize = this.grid.getSize();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int stat = this.grid.getCase(new int[]{i, j});
                if (stat >= this.colors.size())
                    cells[i][j].setFill(Color.web("#F0F0F0"));
                else
                    cells[i][j].setFill(Color.web(this.colors.get(stat)));
            }
        }
    }

    /**
     * Updates the colors in an adapted way for 1D grids.
     */
    private void displayPane1D(){
        int gridSize = this.grid.getSize();
        for (int i = 0; i < gridSize; i++) {
            int stat = this.grid.getCase(new int[]{0, i});
            if (stat >= this.colors.size())
                cells[i][0].setFill(Color.web("#F0F0F0"));
            else
                cells[i][0].setFill(Color.web(this.colors.get(stat)));
        }
    }

    /**
     * Change the state of a cell in the grid for 2D non-Hexagonal grids.
     * @param event The event that triggered the method.
     */
    private void changeStateRectangle(javafx.scene.input.MouseEvent event){
        int gridSize = this.grid.getSize();
        Rectangle eventSource = (Rectangle) event.getSource();
        double col = eventSource.getX()/((double) 650 /gridSize);
        double row = eventSource.getY()/((double) 650 /gridSize);
        int stat = this.colors.indexOf(cb_colors.getValue());

        int pen_size = Integer.parseInt(cb_pen.getValue()) - 1;
        for(int i = -pen_size; i <= pen_size; i++)
            for(int j = -pen_size; j <= pen_size; j++)
                this.grid.setCase(new int[]{(int) row+i, (int) col+j}, stat);
        display();
    }

    /**
     * Initialize the grid in the pane for 2D Hexagonal grids.
     * It uses the hexCells array to store the grid.
     */
    private void initPaneHexa(){
        pane.getChildren().clear();
        this.hexCells = new Polygon[this.grid.getSize()][this.grid.getSize()];
        int gridSize = this.grid.getSize();
        double cos30 = Math.sqrt(3)/2;
        double INVcos30 = 1/cos30;
        double cellSize = 650.0/gridSize;
        double dy = cellSize*(1-cos30);
        int decal = -1;

        for (int i = 0; i < gridSize; i++) {
            if(i%2 == 0)
                decal++;

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
                int etat = this.grid.getCase(new int[]{j, i-j/2%gridSize});
                if (etat >= this.colors.size())
                    tile.setFill(Color.web("#F0F0F0"));
                else
                    tile.setFill(Color.web(this.colors.get(etat)));

                if (j-decal < 0) {
                    tile.setId(j+(gridSize-decal)+" "+i);
                } else {
                    tile.setId((j-decal)+" "+i);
                }
                tile.setStroke(Color.web("#F6F6F6"));
                tile.setStrokeType(StrokeType.INSIDE);
                tile.setStrokeWidth(0.2);
                tile.setSmooth(true);
                tile.setOnMouseClicked(this::changeStatePolygon);
                pane.getChildren().add(tile);
                this.hexCells[j][i] = tile;
            }
        }
    }

    /**
     * Update colors of the hexagons in the 2D grid
     */
    private void displayPaneHexa(){
        int gridSize = this.grid.getSize();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Polygon tile = hexCells[j][i];
                String[] id = tile.getId().split(" ");
                int x = Integer.parseInt(id[1]);
                int y = Integer.parseInt(id[0]);
                int etat = this.grid.getCase(new int[]{y,x});

                if (etat >= this.colors.size())
                    hexCells[i][j].setFill(Color.web("#F0F0F0"));
                else
                    hexCells[i][j].setFill(Color.web(this.colors.get(etat)));
            }
        }
    }

    /**
     * Change the state of a cell in the grid (Hexagonal).
     * It let user draw on the grid.
     * @param event The event that triggered the method.
     */
    private void changeStatePolygon(javafx.scene.input.MouseEvent event){
        int gridSize = this.grid.getSize();
        Polygon tile = (Polygon) event.getSource();
        int x = (int) (tile.getLayoutX()* gridSize/ 650);
        int y = (int) (tile.getLayoutY() * gridSize / (Math.sqrt(3)*325));
        int stat = this.colors.indexOf(cb_colors.getValue());

        this.grid.setCase(new int[]{y, x}, stat);
        displayPaneHexa();
    }

    /**
     * Get the grids stored in the save folder.
     * @return that array.
     */
    private String[] getMaps() {
        File folder = new File("save");
        folder.mkdirs();
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        String[] save = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) save[i] = listOfFiles[i].getName();
        }
        return save;
    }
}
