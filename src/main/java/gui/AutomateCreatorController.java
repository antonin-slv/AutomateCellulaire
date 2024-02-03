package gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import core.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URL;
import java.util.*;

public class AutomateCreatorController implements Initializable{

    @FXML
    private ComboBox<String> cb_select_rule;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_play;

    @FXML
    private Pane pane_neighbors;

    private Map<ArrayList<Integer>,Double> neighbors = new HashMap<>();

    @FXML
    CheckBox is_hexa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        cb_select_rule.getItems().addAll(getRules());
        cb_select_rule.getSelectionModel().select(0);

        btn_play.setOnAction(event -> {
                    try {
                        /*
                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/simulation.fxml"));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);
                        */
                        System.out.println(cb_select_rule.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        is_hexa.setOnAction(event -> {
            neighbors.clear();
            displayNeighbors();
        });

        displayNeighbors();

    }

    private void displayNeighbors() {
        pane_neighbors.getChildren().clear();
        int size = 5;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (is_hexa.isSelected()){
                    if (i == 0 && j == 0) continue;
                    if (i == 1 && j == 0) continue;
                    if (i == 0 && j == 1) continue;
                    if (i == 4 && j == 4) continue;
                    if (i == 3 && j == 4) continue;
                    if (i == 4 && j == 3) continue;
                }
                TextField tf = new TextField();
                tf.setLayoutX(10 + i * 40);
                tf.setLayoutY(10 + j * 40);
                tf.setPrefWidth(40);
                tf.setPrefHeight(40);
                tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        int x = (int) (tf.getLayoutX() - 90) / 40;
                        int y = (int) (tf.getLayoutY() - 90) / 40;
                        if (!newValue.booleanValue()) {
                            try {
                                neighbors.put(new ArrayList<>(Arrays.asList(x, y)), Double.parseDouble(tf.getText()));
                            } catch (NumberFormatException e) {
                                tf.setText("");
                            }
                            System.out.println(neighbors);
                        }
                    }
                });
                pane_neighbors.getChildren().add(tf);
            }
        }
    }

    private String[] getRules() {
        File folder = new File("rules");
        File[] listOfFiles = folder.listFiles();
        String[] rules = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) rules[i] = listOfFiles[i].getName();
        }
        return rules;
    }
}
