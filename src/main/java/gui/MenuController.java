package gui;

import core.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {


    @FXML
    private Button btn_start;
    @FXML
    private Button btn_new_automate;
    @FXML
    private Button btn_load_rule;
    @FXML
    private Button btn_load_map;
    @FXML
    private Button btn_exit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btn_start.setOnAction(event -> {
                    System.out.println("Let's start the game!");
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

        btn_exit.setOnAction(event -> System.exit(0));

        btn_new_automate.setOnAction(event -> {
                    System.out.println("Let's start the game!");
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

        btn_load_rule.setOnAction(event -> System.out.println("LOAD RULE!"));

        btn_load_map.setOnAction(event -> System.out.println("LOAD MAP!"));

    }

}
