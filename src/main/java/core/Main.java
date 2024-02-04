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


/**
 * Main class of the program. It is used to start the application
 * <br />
 * "In the beginning there were nothing, and then man created the Main"
 *                                                  - Leonardo da Vinci
 * @see Application
 * @see javafx
 */
public class Main extends Application {


    /** Stage of the application
     * @see Stage
     */
    @Getter
    private static Stage stage;

    /**
     * Motor of the application
     * @see Moteur
     */
    @Setter
    @Getter
    private static Moteur moteur;


    /**
     * Gson object used to serialize and deserialize objects from and to json
     * @see Gson
     */
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<GameRule>() {}.getType(), new RuleDeserializer())
            .setPrettyPrinting()
            .create();

    /**
     * Main function of the program. launch JavaFX
     * @see Application
     * @see javafx
     * @param args arguments of the program (unused)
     * @throws IOException if there is an error while reading the file

     */
    public static void main(String[] args) throws IOException {
          moteur = new Moteur("rules/JDLV.json", 20);

          launch(); // lancer javafx
    }


    /**
     * Function that is called when launch() is called. It starts the application
     * @param stage window of the application
     * @throws Exception if needed
     */
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


    /**
     * Function to know if the grid is hexagonal
     * @return true if the grid is hexagonal, false otherwise
     */
    public static boolean isHexa(){
        return moteur.getAutomate().isHexa();
    }

    /**
     * Function that sets the hexagonal property of the grid
     * @param hexa true if the grid is hexagonal, false otherwise
     */
    public static void setHexa(boolean hexa){
        moteur.getAutomate().setHexa(hexa);
    }

    /**
     * Function that returns the path to the rules file
     * @return path to the rules file
     */
    public static String getRulesPath(){
        return moteur.getRulesPath();
    }

    public static int getDimension(){
        return moteur.getAutomate().getDimension();
    }

}
