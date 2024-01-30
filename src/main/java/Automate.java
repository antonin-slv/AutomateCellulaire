import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Automate {
    private static final Gson GSON = new Gson();

    private int dimension;
    private List<String> alphabet;
    private int[][] voisinage;
    private EnumMap<GameRule, List<Map<String, Integer>>> regle;

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return GSON.fromJson(reader, Automate.class);
        }
    }

    public int sigma(int[] voisinage, int etat) {
        //Todo : get the next state of the cell at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int[][] getCoordsVoisinage() {
        //Todo : get the neighborhood of the cell at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public enum GameRule {
        @SerializedName("somme") SOMME,
    }
}
