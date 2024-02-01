import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("Rule30.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 100);
        moteur.print();
        moteur.initGrid(new int[][]{{50,1}});
        moteur.print();

        for (int i = 0; i < 50; i++) {
            moteur.update();
            moteur.print();
        }
    }


}
