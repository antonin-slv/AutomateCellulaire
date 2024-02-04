package rules;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * The majority rule.
 * it gives to a given cell the state of its most present neighbor
 * @see rules.GameRule
 */
@AllArgsConstructor
public class MajRule implements GameRule {

    /**
     * Function that returns the state of the most present neighbor
     * @param alphabetSize (unused but fill it to respect the interface)
     * @param voisinage neighborhood of the cell
     * @return the next state of the cell
     */
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        HashMap<Integer,Integer> tab = new HashMap<>();

        for (int voisin : voisinage) {

            tab.merge(voisin, 1, Integer::sum);
        }
        return tab.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(-1);
    }

    /**
     * Function that returns the name of the rule
     * @return the name of the rule
     */
    @Override
    public String toString() {
        return "MajRule";
    }
}
