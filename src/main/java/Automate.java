import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rules.GameRule;
import rules.RuleDeserializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
@AllArgsConstructor
public class Automate {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<GameRule>() {}.getType(), new RuleDeserializer())
            .create();

    private int dimension;
    private List<String> alphabet;
    private int[][] voisinage;
    private GameRule regle;

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return GSON.fromJson(reader, Automate.class);
        }
    }

    public int applyRule(int[] voisinage) {
        return regle.apply(this.alphabet, voisinage);
    }
}
