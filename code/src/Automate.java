import java.util.Map;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class Automate {
    private int dim;
    private int[] Q;
    private Map<Integer,Map<String, Map<String,Integer>>> rules;
    private int[][] voisinage;

    public Automate(int dim, int[] Q, Map<Integer,Map<String, Map<String,Integer>>> rules, int[][] voisinage) {
        this.dim = dim;
        this.Q = Q;
        this.rules = rules;
        this.voisinage = voisinage;
    }

    public Automate(String rules_path) {
        //Todo : read the rules from the file
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getDim() {
        return dim;
    }

    public int sigma(int[] voisinage, int etat) {
        //Todo : get the next state of the cell at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int[] getCoordsVoisinage() {
        //Todo : get the neighborhood of the cell at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
