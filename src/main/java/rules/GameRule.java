package rules;


/**
 * Interface that represents a rule of a cellular automaton
 * <br />
 * A rule is a mainly a function.
 * It is used to determine the next state of a cell based on the state of its neighborhood.
 * States are represented by integers, that are indexes of their names in the alphabet of the automaton
 * @see core.Automate
 */
@FunctionalInterface
public interface GameRule {
    /**
     * Function that takes the size of the alphabet and the neighborhood of a cell and return the next state of the cell
     * @param alphabetSize size of the alphabet
     * @param voisinage neighborhood of the cell
     * @return the next state of the cell
     */
    int apply(int alphabetSize, int[] voisinage);
}
