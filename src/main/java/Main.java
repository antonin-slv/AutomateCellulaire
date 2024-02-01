import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import rules.GameRule;
import rules.RuleDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<GameRule>() {}.getType(), new RuleDeserializer())
            .create();
    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("rules/Major.json");
        URL gridPath = Main.class.getClassLoader().getResource("grids/grid1.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 50);
        moteur.randomizeGrid();
        moteur.print();

    }
}
