package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rules.GameRule;
import rules.RuleDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<GameRule>() {}.getType(), new RuleDeserializer())
            .create();

    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("rules/Major.json");
        URL gridPath = Main.class.getClassLoader().getResource("grids/grid1.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 10);
        moteur.randomizeGrid();
        moteur.print();
        moteur.update();
        moteur.print();

        launch(); // lancer javafx
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../javafx/scene.fxml"));

        Scene scene = new Scene(root);
        // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }
}
