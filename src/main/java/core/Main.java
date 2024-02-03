package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import rules.GameRule;
import rules.RuleDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Getter
    private static Stage stage;

    @Setter
    @Getter
    private static Moteur moteur;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<GameRule>() {}.getType(), new RuleDeserializer())
            .setPrettyPrinting()
            .create();

    public static void main(String[] args) throws IOException {
          moteur = new Moteur("rules/JDLV.json", 20);

          launch(); // lancer javafx
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("javafx/menu.fxml"));

        Scene scene = new Scene(root);
        // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
    }


    public static boolean isHexa(){
        return moteur.getAutomate().isHexa();
    }

    public static void setHexa(boolean hexa){
        moteur.getAutomate().setHexa(hexa);
    }

}
