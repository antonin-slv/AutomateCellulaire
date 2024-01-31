import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        URL rulesPath = Main.class.getClassLoader().getResource("JDLV.json");
        Moteur moteur = new Moteur(Objects.requireNonNull(rulesPath).getPath(), 5);
        moteur.print();
        moteur.initGrid(new int[][]{{18,1}, {17,1}, {16,1}});
        moteur.print();

        for (int i = 0; i < 10; i++) {
            moteur.update();
            moteur.print();
        }

    }


}
