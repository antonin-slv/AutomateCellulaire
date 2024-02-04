package gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import core.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import rules.AvgRule;
import rules.SumRule;
import rules.TabRule;

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

    @FXML
    private Pane pane_tab;

    private final Map<ArrayList<Integer>,Double> neighbors = new LinkedHashMap<>();
    private Map<String, Integer> tab = new HashMap<>();

    @FXML
    private CheckBox is_hexa;

    @FXML
    private TextField tf_filename;

    @FXML
    private Button btn_load;

    private String path;
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        cb_select_rule.getItems().addAll(getRules());
        cb_select_rule.setOnAction(event -> {
            path = "rules/" + cb_select_rule.getValue();
        });
        path = Main.getRulesPath();
        is_hexa.setSelected(Main.getMoteur().getAutomate().isHexa());
        if (Main.getDimension() == 1) {
            is_hexa.setVisible(false);
            is_hexa.setSelected(false);
            tab.clear();
            if (Main.getMoteur().getAutomate().getRegle() instanceof TabRule tabRule) tab = tabRule.getTab();
            displayTab();
        }
        else
            is_hexa.setVisible(true);


        cb_select_rule.getSelectionModel().select(cb_select_rule.getItems().indexOf(path.split("/")[1]));

        btn_play.setOnAction(event -> {
                    save();
                    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                        JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
                        GameController.setAlphabet(json.get("alphabet").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new));
                        GameController.setColors(json.get("colors").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toArray(String[]::new));

                        Main.setHexa(is_hexa.isSelected());

                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("javafx/menu.fxml")));
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
                Main.setMoteur(new Moteur("rules/" + cb_select_rule.getValue(), Main.getMoteur().getGrid().getSize()));
                if (Main.getDimension() == 1) {
                    if (Main.getMoteur().getAutomate().getRegle() instanceof TabRule tabRule) tab = tabRule.getTab();
                    is_hexa.setVisible(false);
                    is_hexa.setSelected(false);
                    displayTab();
                } else {
                    is_hexa.setVisible(true);
                    is_hexa.setSelected(Main.getMoteur().getAutomate().isHexa());
                    displayNeighbors();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        is_hexa.setOnAction(event -> {
            Main.setHexa(is_hexa.isSelected());
            neighbors.clear();
            displayNeighbors();
        });

        btn_save.setOnAction(event -> {
            try {
                save();
                File directory = new File("rules");
                directory.mkdirs();
                Main.getMoteur().getAutomate().toJson("rules/" + tf_filename.getText() + ".json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (Main.getDimension() != 1) displayNeighbors();

    }

    private void save() {
        if (Main.getDimension() == 1) {
            if (Main.getMoteur().getAutomate().getRegle() instanceof TabRule tabRule) {
                tabRule.setTab(tab);
            }
        } else {
            List<List<Integer>> newNeighbors = new ArrayList<>();
            List<Double> newWeights = new ArrayList<>();
            for (var entry : neighbors.entrySet()) {
                newNeighbors.add(entry.getKey());
                newWeights.add(entry.getValue());
            }
            int[][] voisinage = newNeighbors.stream().map(l -> l.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);
            System.out.println(Arrays.deepToString(voisinage));
            System.out.println(newWeights);
            System.out.println(Arrays.deepToString(Main.getMoteur().getAutomate().getVoisinage()));
            Main.getMoteur().getAutomate().setVoisinage(voisinage);
            double sum = newWeights.stream().mapToDouble(Double::doubleValue).sum();
            newWeights.replaceAll(aDouble -> aDouble / sum * (newNeighbors.size() - 1));
            if (Main.getMoteur().getAutomate().getRegle() instanceof SumRule sumRule) {
                System.out.println(sumRule.getWeightNeighbour());
                sumRule.setWeightNeighbour(newWeights);
            }
            if (Main.getMoteur().getAutomate().getRegle() instanceof AvgRule avgRule) {
                avgRule.setWeightNeighbour(newWeights);
            }
        }
    }

    private void displayTab() {
        int size = 5;
        pane_tab.getChildren().clear();
        pane_neighbors.getChildren().clear();
        int i = 0;
        System.out.println(tab);
        for (var k : tab.keySet()) {
            CheckBox cb = new CheckBox();
            Label lb = new Label(k);
            lb.setLayoutX(10 + i * 40);
            lb.setLayoutY(10);
            cb.setLayoutX(10 + i * 40);
            cb.setLayoutY(30);
            cb.setPrefWidth(40);
            cb.setPrefHeight(40);
            i++;
            if (tab.get(k) == 1) {
                cb.setSelected(true);
            } else {
                cb.setSelected(false);
            }
            cb.setOnAction (event -> {
                if (cb.isSelected()) {
                    tab.put(k, 1);
                } else {
                    tab.put(k, 0);
                }
            });
            pane_tab.getChildren().add(cb);
            pane_tab.getChildren().add(lb);
        }
    }

    private void displayNeighbors() {
        int size = 5;
        pane_neighbors.getChildren().clear();

        var rule = Main.getMoteur().getAutomate().getRegle();
        if (rule instanceof AvgRule || rule instanceof SumRule) {

            List<List<Integer>> oldNeighbors = Arrays.stream(Main.getMoteur().getAutomate().getVoisinage()).map(l -> Arrays.stream(l).boxed().toList()).toList();
            List<Double> oldWeights;
            if (rule instanceof AvgRule)
                oldWeights = ((AvgRule) rule).getWeightNeighbour();
            else
                oldWeights = ((SumRule) rule).getWeightNeighbour();

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
                    tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        int x = (int) (tf.getLayoutX() - 90) / 40;
                        int y = (int) (tf.getLayoutY() - 90) / 40;
                        if (!newValue) {
                            try {
                                neighbors.put(new ArrayList<>(Arrays.asList(y, x)), Double.parseDouble(tf.getText()));
                            } catch (NumberFormatException e) {
                                tf.setText("");
                            }
                        }
                    });
                    pane_neighbors.getChildren().add(tf);
                }
            }
        } else {
            List<List<Integer>> oldNeighbors = Arrays.stream(Main.getMoteur().getAutomate().getVoisinage()).map(l -> Arrays.stream(l).boxed().toList()).toList();
            neighbors.clear();
            for (List<Integer> oldNeighbor : oldNeighbors) {
                neighbors.put(new ArrayList<>(oldNeighbor), 1.0);
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
        assert listOfFiles != null;
        String[] rules = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) rules[i] = listOfFiles[i].getName();
        }
        return rules;
    }
}
