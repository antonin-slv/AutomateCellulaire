package gui;

import core.Main;
import core.Moteur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    Moteur moteur ;
    private int gridSize = 100;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button btn_update_once;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            URL rulesPath = Main.class.getClassLoader().getResource("rules/Major.json");
            this.moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), gridSize);
            this.moteur.randomizeGrid();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        initializeGridPane();

        btn_update_once.setOnAction(event -> {
            this.moteur.update();
            updateGrid();
        });
    }

    private void initializeGridPane(){

        int cellSize = 600/gridSize;

        // Set up the rows and columns.
        for (int i = 0; i < gridSize; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
            gridPane.getRowConstraints().add(new RowConstraints(cellSize));
        }

        //Add an image to each cell with on OnClick mouse event.
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                initializeGridRectangles(i,j);
            }
        }
    }

    private void initializeGridRectangles(int row, int col) {

        int cellSize = 600/gridSize;

        // All cells will initially be dead, build a Rectangle for each.
        Rectangle cellRect = new Rectangle();
        cellRect.setHeight(cellSize);
        cellRect.setWidth(cellSize);
        int etat = this.moteur.getEtat(new int[]{row, col});
        if (etat == 0){
            cellRect.setFill(Color.WHITE);
        }
        else{
            cellRect.setFill(Color.BLACK);
        }
        cellRect.setStroke(Color.web("#F6F6F6"));
        cellRect.setStrokeType(StrokeType.INSIDE);
        cellRect.setStrokeWidth(0.1);
        cellRect.setSmooth(true);

        // Add mouse event to this rectangle..
        /*
        cellRect.setOnMouseClicked(this::mouseClickedEvent);
        cellRect.setOnDragDetected(this::mouseDragStartEvent);
        cellRect.setOnMouseDragOver(this::mouseDragOverEvent);
        cellRect.setOnMouseDragReleased(this::mouseDragEndEvent);
        */

        // Add the Rectangle to the grid at the col/row.
        gridPane.add(cellRect, col, row);
    }

    private void updateGrid() {
        //Update each node in the grid.
        for (Node child : gridPane.getChildren()) {
            //Get the row and col of the current grid node.
            int col = GridPane.getColumnIndex(child);
            int row = GridPane.getRowIndex(child);
            Rectangle cellRect = (Rectangle)child;

            int etat = this.moteur.getEtat(new int[]{row, col});
            if(etat == 1) {
                cellRect.setFill(Color.RED);
            } else if (etat == 2) {
                cellRect.setFill(Color.BLUE);
            } else {
                cellRect.setFill(Color.GREEN);
            }
        }
    }
}
