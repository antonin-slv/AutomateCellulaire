package rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



/**
 * The average rule.
 * it give to a given cell the average state of its neighbors (ponderated by the weightNeighbour)
 * To use this specific Rule, the alphabet[0] String must be parsable to an int, that will be the maximum state value.
 * @see rules.GameRule
 */
@Getter
@Setter
@AllArgsConstructor
public class AvgRule implements GameRule {

    /** List of the weight of each neighbor (in order of definition) */
    private List<Double> weightNeighbour;
    /** Total weight of the neighbors (unused) */
    private double poidTotal;

    /**
     * Function that returns the ponderated average of an array of int
     * @param alphabetSize size of the alphabet
     * @param voisinage neighborhood of the cell
     * @return the next state of the cell
     */
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        double average = 0.0;
        int i =0;
        double pd2 = 0.0;
        for (int voisin : voisinage) {
            pd2 += weightNeighbour.get(i);
            average += voisin * weightNeighbour.get(i);
            i++;
        }
        assert pd2 != 0;
        average = average / pd2;

        return (int) average;

    }

    /**
     * Function that returns the name of the rule
     * @return the name of the rule
     */
    @Override
    public String toString() {
        return "AvgRule";
    }
}
