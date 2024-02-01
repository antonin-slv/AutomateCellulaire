package rules;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@AllArgsConstructor
public class SumRule implements GameRule {
    private List<Map<String, List<Double>>> tab;
    private List<Double> weightType;
    private List<Double> weightNeighbour;

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
        //SOMME est fait pour marcher avec un état binaire. Si on a plus d'état, ça casse.
        for (var entry : miniRules.entrySet()) {
            if (this.isInRange(entry.getKey(), somme)) {
                List<Double> proba = entry.getValue();
                double rand = Math.random();

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
