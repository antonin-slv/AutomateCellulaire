package core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rules.GameRule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Getter
@AllArgsConstructor
public class Automate {

    @Getter
    @Setter
    private boolean isHexa=false;

    @Getter
    private int dimension;
    @Setter
    private List<String> alphabet;
    @Setter
    private List<String> colors;
    @Setter
    private int[][] voisinage;
    private GameRule regle;

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return Main.GSON.fromJson(reader, Automate.class);
        }
    }

    public void toJson(String rulesPath) throws IOException {
        try (FileWriter file = new FileWriter(rulesPath)) {
            Main.GSON.toJson(this, file);
        }
    }

    public int applyRule(int[] voisinage) {
        return regle.apply(this.alphabet.size(), voisinage);
    }
}
