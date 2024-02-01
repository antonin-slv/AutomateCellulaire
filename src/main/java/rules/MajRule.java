package rules;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class MajRule implements GameRule {
    private List<Double> weightNeighbour;

    @Override
    public int apply(List<String> alphabet, int[] voisinage) {
        HashMap<String,Integer> tab = new HashMap<>();
        for (int i = 0; i < alphabet.size(); i++) {
            tab.put(String.valueOf(i),0);
        }
        for (int i = 0; i < voisinage.length; i++) {
            if (voisinage[i] >= alphabet.size())//si on a pas de règle pour cet état, on crash !
                throw new UnsupportedOperationException("état non défini");
            tab.put(String.valueOf(voisinage[i]),tab.get(String.valueOf(voisinage[i]))+1);
        }
        return tab.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey().charAt(0)-'0';
    }
}
