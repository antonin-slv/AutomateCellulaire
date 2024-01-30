import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Automate {
    private static final Gson GSON = new Gson();

    public int dimension;
    public List<String> alphabet;
    public int[][] voisinage;
    public EnumMap<GameRule, List<Map<String, Integer>>> regle;

    public Automate(int dimension, List<String> alphabet, int[][] voisinage, EnumMap<GameRule, List<Map<String, Integer>>> regle) {
        this.dimension = dimension;
        this.alphabet = alphabet;
        this.voisinage = voisinage;
        this.regle = regle;
    }

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return GSON.fromJson(reader, Automate.class);
        }
    }

    public int getDim() {
        return this.dimension;
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
