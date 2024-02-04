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


/**
 * Class that represents a Cellular automaton
 * <br />
 * An automaton is defined by:
 * <ul>
 *     <li>Its dimension</li>
 *     <li>The neighborhood of each cell</li>
 *     <li>Its rule : how neighborhood will tell what is the next state of a cell</li>
 *     <li>Its alphabet (the state each cell can take)</li>
 *</ul>
 * and have other properties(colors and isHexa) used for display purposes
 */
@Getter
@AllArgsConstructor
public class Automate {
    /**
     * boolean that indicates if the automaton is hexagonal
     */
    @Getter
    @Setter
    private boolean isHexa=false;

    /**
     * dimension of the grid
     */
    @Getter
    private int dimension;

    /**
     * List of the names of each state of the automaton
     * if it is a continuous automaton, the list contains only the max value of the automaton
     */
    @Setter
    private List<String> alphabet;
    /**
     * List of the colors of each state of the automaton
     * if it is a continuous automaton, the list contains only the number of colors used
     */
    @Setter
    private List<String> colors;

    /**
     * Array of the relative positions of the neighbors of the cell
     */
    @Setter
    private int[][] voisinage;

    /**
     * Rule of the automaton
     */
    @Getter
    private GameRule regle;


    /**
     * Function that creates an automaton object from a json file containing its definition
     * @param rulesPath path of the json file containing the definition of the automaton
     * @return automaton The given automaton
     *
     * @throws IOException if there is an error while reading the file
     */
    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return Main.GSON.fromJson(reader, Automate.class);
        }
    }

    /**
     * Function that saves the configuration of the automaton in a json file
     *
     * @param rulesPath path of the json file where the configuration will be saved
     * @throws IOException if there is an error while writing the file
     */
    public void toJson(String rulesPath) throws IOException {
        try (FileWriter file = new FileWriter(rulesPath)) {
            Main.GSON.toJson(this, file);
        }
    }

    /**
     * Function that returns the value of a cell according to its neighborhood
     *
     * @param voisinage Tab of the neighborhood of the cell
     * @return new value of the cell
     */
    public int applyRule(int[] voisinage) {
        return regle.apply(this.alphabet.size(), voisinage);
    }
}
