package rules;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class AvgRule implements GameRule {

    double [] weightNeighbour;
    double poidTotal;
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        double average = 0.0;
        int i =0;
        for (int voisin : voisinage) {

            average += voisin * weightNeighbour[i];
            i++;
        }

        average = average / poidTotal;

        return (int) ((double)average);

    }
}
