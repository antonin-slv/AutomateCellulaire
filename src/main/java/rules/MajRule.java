package rules;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MajRule implements GameRule {
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        HashMap<Integer,Integer> tab = new HashMap<>();

        for (int voisin : voisinage) {

            tab.merge(voisin, 1, Integer::sum);
        }
        return tab.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(-1);
    }
}
