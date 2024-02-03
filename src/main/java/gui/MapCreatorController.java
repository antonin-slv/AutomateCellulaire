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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapCreatorController implements Initializable {

    Grid grid;

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
    private ComboBox<String> cb_colors;
    @FXML
    private ComboBox<String> cb_pen;
    @FXML
    private ComboBox<String> cb_replace1;
    @FXML
    private ComboBox<String> cb_replace2;
    @FXML
    private Button btn_replace;
    @FXML
    private Button btn_random;
    @FXML
    private Button btn_back;


    public void initialize(URL url, ResourceBundle rb) {


        this.grid = Main.getMoteur().getGrid();

        btn_back.setOnAction(event -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/menu.fxml"));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_random.setOnAction(event -> Main.getMoteur().randomizeGrid());

        btn_load.setOnAction(event -> {
                    try {
                        this.grid = Grid.fromJson(cb_load.getValue());
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
    }
}
