package rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The tab rule.
 * use Map( arrangement => nextValue) containing all possibilities of neighborhood to determine the next state of the cell
 * !!! arrangement must be in the same order as the declaration of the neighbours
 * plus, the alphabet size must be 9 or less otherwise, the rule will be unpredictable or crash.
 * In facts, we guarantee nothing beyond 2 states
 * @see rules.GameRule
 */
@Setter
@Getter
@AllArgsConstructor
public class TabRule implements GameRule {
    /**
     * Map of the next state of the cell given the neighborhood
     * The key is In form "state1state2state3...stateN" where stateI is the state of the Ith neighbor
     * if the int of a state is greater or equal to 10, the rule will be unpredictable
     */
    private Map<String, Integer> tab;

    /**
     * Function that returns the next state of the cell given the one of its neighbors
     * @param alphabetSize size of the alphabet
     * @param voisinage neighborhood of the cell
     * @return the next state of the cell
     */
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        String key = Arrays.stream(voisinage).mapToObj(String::valueOf).collect(Collectors.joining());

        return tab.getOrDefault(key, -1);
    }

    /**
     * Function that returns the name of the rule
     * @return the name of the rule
     */
    @Override
    public String toString() {
        return "TabRule";
    }
}
