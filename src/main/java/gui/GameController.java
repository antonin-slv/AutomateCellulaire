package gui;

import core.Main;
import core.Moteur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    Moteur moteur ;
    private int gridSize = 100;

    @FXML
    private Pane pane;

    @FXML
    private Button btn_update_once;

    @FXML
    private Button btn_retour;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            URL rulesPath = Main.class.getClassLoader().getResource("rules/Major.json");
            this.moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), gridSize);
            //this.moteur.initGrid(new int[][]{{0, 1}, {1, 1}, {2, 1}});
            this.moteur.randomizeGrid();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        displayPaneHexa();

        btn_update_once.setOnAction(event -> {
            this.moteur.update();
            pane.getChildren().clear();
            displayPaneHexa();
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

    private void displayPane(){

        int cellSize = 600/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Rectangle cellRect = new Rectangle();
                cellRect.setHeight(cellSize);
                cellRect.setWidth(cellSize);
                int etat = this.moteur.getEtat(new int[]{i, j});
                if (etat == 0){
                    cellRect.setFill(Color.WHITE);
                }
                else{
                    cellRect.setFill(Color.BLACK);
                }
                cellRect.setStroke(Color.web("#F6F6F6"));
                cellRect.setStrokeType(StrokeType.INSIDE);
                cellRect.setStrokeWidth(0.2);
                cellRect.setSmooth(true);
                cellRect.setX(j*cellSize);
                cellRect.setY(i*cellSize);
                pane.getChildren().add(cellRect);
            }
        }
    }
    private void displayPaneHexa(){

        int cellSize = 600/gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Polygon tile = new Polygon();
                double x,y;
                if (j%2 == 1) {
                    x = i*cellSize+cellSize/2;
                } else {
                    x = i*cellSize;
                }
                y = j*cellSize+cellSize/2;
                tile.getPoints().addAll(new Double[]{
                        x, y - cellSize/2,
                        x - cellSize/2*Math.sqrt(3)/2, y-cellSize/4,
                        x - cellSize/2*Math.sqrt(3)/2, y+cellSize/4,
                        x, y + cellSize/2,
                        x + cellSize/2*Math.sqrt(3)/2, y+cellSize/4,
                        x + cellSize/2*Math.sqrt(3)/2, y-cellSize/4
                });
                int etat = this.moteur.getEtat(new int[]{i, j});
                if (etat == 0){
                    tile.setFill(Color.WHITE);
                }
                else{
                    tile.setFill(Color.BLACK);
                }
                tile.setStroke(Color.web("#F6F6F6"));
                tile.setStrokeType(StrokeType.INSIDE);
                tile.setStrokeWidth(0.2);
                tile.setSmooth(true);
                pane.getChildren().add(tile);
            }
        }
    }
}
