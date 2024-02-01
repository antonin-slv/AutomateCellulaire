package rules;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TabRule implements GameRule {
    private final Map<String, Integer> tab;

    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        String key = Arrays.stream(voisinage).mapToObj(String::valueOf).collect(Collectors.joining());

        return tab.getOrDefault(key, -1);
    }
}
