package core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rules.GameRule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Getter
@AllArgsConstructor
public class Automate {

    private int dimension;
    private List<String> alphabet;
    private int[][] voisinage;
    private GameRule regle;

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return Main.GSON.fromJson(reader, Automate.class);
        }
    }

    public int applyRule(int[] voisinage) {
        return regle.apply(this.alphabet.size(), voisinage);
    }
}
