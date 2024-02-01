package rules;

import java.util.List;

@FunctionalInterface
public interface GameRule {
    int apply(List<String> alphabet, int[] voisinage);
}
