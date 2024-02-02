package rules;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DirRule implements GameRule {
    @Override
    public int apply(int alphabetSize, int[] voisinage) {
        //on récupère l'état de la cellule
        int etat = voisinage[0];

        //force à vous les gars, j'en ai marre de cette merde, je fais le rapport

        return etat;

    }
}
