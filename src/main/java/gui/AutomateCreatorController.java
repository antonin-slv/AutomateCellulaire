package gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import core.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
public class AutomateCreatorController implements Initializable{

    @FXML
    private ComboBox<String> cb_select_rule;

    @FXML
    private Button btn_save;

    @FXML
    private Button btn_play;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cb_select_rule.getItems().addAll(getRules());
        cb_select_rule.getSelectionModel().select(0);

        EventHandler<ActionEvent> event_cmb =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e)
                    {
                        try (BufferedReader reader = new BufferedReader(new FileReader("config.json"))) {
                            JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
                            json.add("rule", new JsonPrimitive(cb_select_rule.getValue()));
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.json"))) {
                                Main.GSON.toJson(json, writer);
                            }
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };

        cb_select_rule.setOnAction(event_cmb);

        btn_play.setOnAction(event -> {
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
