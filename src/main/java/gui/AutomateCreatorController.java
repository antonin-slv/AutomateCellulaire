package gui;

import com.google.gson.JsonElement;
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
import rules.AvgRule;
import rules.SumRule;

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
    private CheckBox is_hexa;

    @FXML
    private TextField tf_filename;

    @FXML
    private Button btn_load;
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        cb_select_rule.getItems().addAll(getRules());
        cb_select_rule.getSelectionModel().select(0);

        btn_play.setOnAction(event -> {
                    save();
                    try (BufferedReader reader = new BufferedReader(new FileReader("rules/" + cb_select_rule.getValue()))) {
                        JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
                        GameController.setAlphabet(json.get("alphabet").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new));
                        GameController.setColors(json.get("colors").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new));

                        Main.setHexa(is_hexa.isSelected());

                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/simulation.fxml"));
                        Scene scene = new Scene(root);
                        Main.getStage().setScene(scene);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
        );

        btn_load.setOnAction(event -> {
            try {
                Main.setMoteur(new Moteur("rules/" + cb_select_rule.getValue(), 150));
                Main.setHexa(is_hexa.isSelected());
                displayNeighbors();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        is_hexa.setOnAction(event -> {
            neighbors.clear();
            displayNeighbors();
        });

        btn_save.setOnAction(event -> {
            try {
                Main.getMoteur().getAutomate().toJson("rules/" + tf_filename.getText() + ".json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        displayNeighbors();

    }

    private void save() {
        List<List<Integer>> newNeighbors = new ArrayList<>();
        List<Double> newWeights = new ArrayList<>();
        for (var entry : neighbors.entrySet()) {
            newNeighbors.add(entry.getKey());
            newWeights.add(entry.getValue());
        }
        int[][] voisinage = newNeighbors.stream().map(l -> l.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);
        Main.getMoteur().getAutomate().setVoisinage(voisinage);
        System.out.println(Arrays.deepToString(voisinage));
        System.out.println(Arrays.deepToString(Main.getMoteur().getAutomate().getVoisinage()));
        double sum = newWeights.stream().mapToDouble(Double::doubleValue).sum();
        for (int i = 0; i < newWeights.size(); i++) {
            newWeights.set(i, newWeights.get(i) / sum * (newNeighbors.size()-1));
        }
        Main.getMoteur().getAutomate().setVoisinage(voisinage);
        if (Main.getMoteur().getAutomate().getRegle() instanceof SumRule sumRule) {
            sumRule.setWeightNeighbour(newWeights);
        }
        if (Main.getMoteur().getAutomate().getRegle() instanceof AvgRule avgRule) {
            avgRule.setWeightNeighbour(newWeights);
        }
    }

    private void displayNeighbors() {
        int size = 5;
        pane_neighbors.getChildren().clear();
        if (Main.getMoteur().getAutomate().getRegle() instanceof SumRule sumRule) {
            List<List<Integer>> oldNeighbors = Arrays.stream(Main.getMoteur().getAutomate().getVoisinage()).map(l -> Arrays.stream(l).boxed().toList()).toList();
            List<Double> oldWeights = sumRule.getWeightNeighbour();
            neighbors.clear();
            for (int i = 0; i < oldNeighbors.size(); i++) {
                neighbors.put(new ArrayList<>(oldNeighbors.get(i)), oldWeights.get(i));
            }
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
                    if (neighbors.containsKey(new ArrayList<>(Arrays.asList(j-2, i-2)))) {
                        tf.setText(neighbors.get(new ArrayList<>(Arrays.asList(j-2, i-2))).toString());
                    }
                    tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            int x = (int) (tf.getLayoutX() - 90) / 40;
                            int y = (int) (tf.getLayoutY() - 90) / 40;
                            if (!newValue.booleanValue()) {
                                try {
                                    neighbors.put(new ArrayList<>(Arrays.asList(y, x)), Double.parseDouble(tf.getText()));
                                } catch (NumberFormatException e) {
                                    tf.setText("");
                                }
                            }
                        }
                    });
                    pane_neighbors.getChildren().add(tf);
                }
            }
        } else {
            List<List<Integer>> oldNeighbors = Arrays.stream(Main.getMoteur().getAutomate().getVoisinage()).map(l -> Arrays.stream(l).boxed().toList()).toList();
            neighbors.clear();
            for (int i = 0; i < oldNeighbors.size(); i++) {
                neighbors.put(new ArrayList<>(oldNeighbors.get(i)), 1.0);
            }
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
                    CheckBox cb = new CheckBox();
                    cb.setLayoutX(10 + i * 40);
                    cb.setLayoutY(10 + j * 40);
                    cb.setPrefWidth(40);
                    cb.setPrefHeight(40);
                    if (neighbors.containsKey(new ArrayList<>(Arrays.asList(j-2, i-2)))) {
                        cb.setSelected(true);
                    }
                    cb.setOnAction(event -> {
                        int x = (int) (cb.getLayoutX() - 90) / 40;
                        int y = (int) (cb.getLayoutY() - 90) / 40;
                        if (cb.isSelected()) {
                            neighbors.put(new ArrayList<>(Arrays.asList(y, x)), 1.0);
                        } else {
                            neighbors.remove(new ArrayList<>(Arrays.asList(y, x)));
                        }
                        System.out.println(neighbors);
                    });
                    pane_neighbors.getChildren().add(cb);
                }
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
