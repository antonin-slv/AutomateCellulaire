import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Automate {
    private static final Gson GSON = new Gson();

    private int dimension;
    private List<String> alphabet;
    private int[][] voisinage;
    private EnumMap<GameRule, List<Map<String, Integer>>> regle;

    public static Automate fromJson(String rulesPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rulesPath))) {
            return GSON.fromJson(reader, Automate.class);
        }
    }

    /** !!!!!à tester un jour étonnamment.
     * Une fonction qui calcule le prochain état d'une cellule en fonction de celui de ses voisins et d'elle même
     * @param voisinage : Tableau contenant l'état des cellules voisines
     * @return : Etat de la cellule à l'étape suivante
     */
    public int sigma(int[] voisinage) {
        for (Map.Entry<GameRule, List<Map<String, Integer>>> gameRuleList : regle.entrySet()) {
            //on règle le cas de la somme
            if (gameRuleList.getKey() == GameRule.SOMME) {
                int etat = voisinage[0];
                int somme = Arrays.stream(voisinage).skip(1).sum();

                //on récupère la éta-ième règle de la somme (il y a autant de règle que d'état différent)
                //on vérifie que état est bien une clef

                Map<String, Integer> miniRules;
                if (etat >= gameRuleList.getValue().size()){
                    //si on a pas de règle pour cet état, on prend la règle de l'état 0 (par défaut)
                    miniRules = gameRuleList.getValue().get(0);
                }
                else miniRules = gameRuleList.getValue().get(etat);
                //SOMME est fait pour marcher avec un état binaire. Si on a plus d'état, ça casse.
                for (String clef : miniRules.keySet()) {
                    int min = Integer.parseInt(clef.split(":")[0]);
                    int max = Integer.parseInt(clef.split(":")[1]);
                    if (somme >= min && somme <= max) {
                        return miniRules.get(clef);
                    }
                }
            }
            if(gameRuleList.getKey() == GameRule.TABLE) {
                Map<String, Integer> miniRules;
                miniRules = gameRuleList.getValue().get(0);
                String choix = "" + voisinage[0] + voisinage[1] + voisinage[2];
                return miniRules.get(choix);
            }
        }
        throw new UnsupportedOperationException("We didn't set that yet");
    }

    public enum GameRule {
        @SerializedName("somme") SOMME,
        @SerializedName("table") TABLE,
    }
}
