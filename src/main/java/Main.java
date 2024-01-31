import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("rule30.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 300);
        moteur.print();
        moteur.initGrid(new int[][]{ {150,1}});
        moteur.print();

        for (int i = 0; i < 150; i++) {
            moteur.update();
            moteur.print();
        }
    }


}
