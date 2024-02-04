package rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * The sum rule.
 * Calculate the sum of the states of the neighbors(the value of each state is declared in weightType).
 * This sum is pondered by the weight of the neighbors.
 * Then, a tab of rules is used to determine the next state of the cell given the calculated sum.
 * it comes in a form of a list of map where each map is a rule.
 * The rule used is given by the state of the first neighbor. It it is in state 0,use rule 0. State 1, rule 1. Etc...
 * a rule is int this form : "min:max" -> [p1,p2,p3,...]
 * where p1,p2,p3... are the probabilities of the next state, whose sum must be 1
 * min and max are the bounds of the sum to apply the rule
 * @see rules.GameRule
 */
@Getter
@Setter
@AllArgsConstructor
public class SumRule implements GameRule {

    /** List of the tab of rules (contains the rules for each state of neighbour[0]) */
    private List<Map<String, List<Double>>> tab;
    /**
     * List of the weight of each state//type (same order as the alphabet)
     * @see core.Automate
     */
    private List<Double> weightType;
    /** List of the weight of each neighbor (same order as the neighbours) */
    private List<Double> weightNeighbour;

    /**
     * Function that returns the next state of the cell given the one of its neighbors
     *
     * @param alphabetSize size of the alphabet
     * @param voisinage neighborhood of the cell
     * @return the next state of the cell
     */
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        int etat = voisinage[0];

        double somme = IntStream.range(0, voisinage.length)
                .mapToDouble(i -> weightType.get(voisinage[i]) * weightNeighbour.get(i))
                .sum();

        //on récupère la éta-ième règle de la somme (il y a autant de règle que d'état différent)
        //on vérifie que état est bien une clef

        if (etat >= alphabetSize)//si on a pas de règle pour cet état, on crash !
            throw new UnsupportedOperationException("état non défini");

        Map<String, List<Double>> miniRules = tab.get(etat);
        double rand = Math.random();
        for (var entry : miniRules.entrySet()) {
            if (this.isInRange(entry.getKey(), somme)) {
                List<Double> proba = entry.getValue();
                double sum = 0.0;
                for (int i = 0; i < alphabetSize; i++) {
                    sum += proba.get(i);
                    if (rand <= sum) {
                        return i;
                    }
                }
            }
        }

        throw new UnsupportedOperationException("état non défini");
    }

    /**
     * Function that checks if a value is in a given range
     *
     * @param range String of the form "min:max" where min and max are the bounds of the range
     * @param value the value to check
     * @return true if the value is in the range, false otherwise
     * whill return false if the range is not well formatted
     */

    private boolean isInRange(String range, double value) {
        try {
            int min = Integer.parseInt(range.split(":")[0]);
            int max = Integer.parseInt(range.split(":")[1]);
            return value >= min && value <= max;
        } catch (Exception e) {
            System.err.println("Range `" + range + "` is not well formatted");
            return false;
        }
    }
}
