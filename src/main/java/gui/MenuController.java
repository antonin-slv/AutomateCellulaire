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
import java.util.ResourceBundle;

public class MenuController implements Initializable {


    @FXML
    private Button btn_start;
    @FXML
    private Button btn_modify_automate;
    @FXML
    private Button btn_modify_map;
    @FXML
    private Button btn_exit;
    @FXML
    private Label lbl_info_automate;
    @FXML
    private Label lbl_info_map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_exit.setOnAction(event -> System.exit(0));

        btn_start.setOnAction(event -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/simulation.fxml"));
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
                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/automateCreator.fxml"));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_modify_map.setOnAction(event -> System.out.println("LOAD MAP!"));

        lbl_info_automate.setText(Main.getMoteur() == null ? "No rules loaded" : Main.getMoteur().getRules());

//        lbl_info_map.setText();
    }

}
