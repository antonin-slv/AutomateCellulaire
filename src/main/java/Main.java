import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("JDLV.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath());
        moteur.print();
        moteur.update();

//        Grid grille = new Grid(3, 2, new int[]{1, 2, 3, 4, 5, 6, 7, 8});
//        System.out.println(grille.getCase(new int[]{1, 1, 0}));
    }
}
