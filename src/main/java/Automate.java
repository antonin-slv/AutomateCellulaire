import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
@AllArgsConstructor
public class Automate {
    private static final Gson GSON = new Gson();

    private int dimension;
    private List<String> alphabet;
    private int[][] voisinage;
    private EnumMap<GameRule, Map<String,Object>> regle;

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
        //on règle le cas de la somme
        // {"filtre": [{"filtre": [0,0,1,0]}]} on accède à la règle de la somme
        if (regle.containsKey(GameRule.SOMME)) {
            List<Map <String, List<Double>>> gameRuletab;
            List<Double> weight;
            try{
                //on récupère la règle de la somme
                gameRuletab = (List<Map<String, List<Double>>>) regle.get(GameRule.SOMME).get("tab");
                //on récupère la liste des poids
                weight = (List<Double>) regle.get(GameRule.SOMME).get("weight");
            }
            catch (ClassCastException e){
                throw new UnsupportedOperationException("json mal formé");
            }

            int etat = voisinage[0];
            double somme = 0.0;

            for (int i : voisinage) {
                somme = weight.get(i) + somme;
            }

            //on récupère la éta-ième règle de la somme (il y a autant de règle que d'état différent)
            //on vérifie que état est bien une clef

            if (etat >= alphabet.size())//si on a pas de règle pour cet état, on crash !
                throw new UnsupportedOperationException("état non défini");

            Map<String, List<Double>> miniRules = gameRuletab.get(etat);
            List<Double> proba;
            //SOMME est fait pour marcher avec un état binaire. Si on a plus d'état, ça casse.
            for (String clef : miniRules.keySet()) {
                int min = Integer.parseInt(clef.split(":")[0]);
                int max = Integer.parseInt(clef.split(":")[1]);
                if (somme >= min && somme <= max) {

                    proba = miniRules.get(clef);
                    float rand = new Random().nextFloat();

                    double sum = 0.0;
                    for(int i = 0; i < this.alphabet.size(); i++){
                        sum += proba.get(i);
                        if(rand <= sum){
                            return i;
                        }
                    }
                }
            }

            throw new UnsupportedOperationException("état non défini");
        }
        // pour toi mon léo bébou-san
        if(regle.containsKey(GameRule.TABLE)) return (int)java.lang.Math.round((Double)regle.get(GameRule.TABLE).get("" + voisinage[0] + voisinage[1] + voisinage[2]));


        throw new UnsupportedOperationException("We didn't set that yet");
    }

    public enum GameRule {
        @SerializedName("somme") SOMME,
        @SerializedName("table") TABLE,
    }
}
