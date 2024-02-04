package gui;

import core.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static gui.GameController.is_ready;

/**
 * Controller of the menu.
 * It allows to start the simulation and to navigate to the map creator and the automaton creator.
 */
public class MenuController implements Initializable {

    /** Button to start the simulation */
    @FXML
    private Button btn_start;
    /** Button to go to the automaton Editor */
    @FXML
    private Button btn_modify_automate;
    /** Button to go to the map Editor */
    @FXML
    private Button btn_modify_map;
    /** Button to exit the application */
    @FXML
    private Button btn_exit;
    /** Label that shows information about the automaton */
    @FXML
    private Label lbl_info_automate;
    /** Label that shows information about the map */
    @FXML
    private Label lbl_info_map;
    @FXML
    private Label lbl_info;


    /**
     * Function that initialize the menu.
     * It sets the action of the buttons and information about the automaton and the map engaged.
     * @param url the url of the fxml file
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_exit.setOnAction(event -> System.exit(0));

        btn_start.setDisable(!is_ready());
        lbl_info.setVisible(!is_ready());

        btn_start.setOnAction(event -> {
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/simulation.fxml")));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_modify_automate.setOnAction(event -> {
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/automateCreator.fxml")));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_modify_map.setOnAction(event -> {
                    try {
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/mapCreator.fxml")));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        lbl_info_automate.setText(Main.getMoteur().getAutomate() == null ? "No rules loaded" : Main.getMoteur().getRules());

        lbl_info_map.setText(Main.getMoteur() == null ? "No map loaded" :  "dimension : " + Main.getMoteur().getGrid().getDim() + "\nsize : " + Main.getMoteur().getGrid().getSize() +" * "+ Main.getMoteur().getGrid().getSize()
                + (Main.getMoteur().getGrid().isEmpty()? "\nEmpty" : "\nNot empty"));

    }

}
