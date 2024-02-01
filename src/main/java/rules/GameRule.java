package rules;

@FunctionalInterface
public interface GameRule {
    int apply(int alphabetSize, int[] voisinage);
}
