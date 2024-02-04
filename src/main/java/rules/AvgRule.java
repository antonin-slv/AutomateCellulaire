package rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class AvgRule implements GameRule {

    private List<Double> weightNeighbour;
    private double poidTotal;
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

        average = average / pd2;

        return (int) ((double)average);

    }
}
