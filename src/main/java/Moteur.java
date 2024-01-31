import java.io.IOException;

public class Moteur {
    private final Automate automate;
    private final Grid grid;

    public Moteur(String rulesPath) throws IOException {
        this.automate = Automate.fromJson(rulesPath);
        this.grid = new Grid(automate.getDimension(), 3);
    }

    public void update() {

        //grid_temp est la grille temporaire qui va être remplie
        Grid grid_temp = new Grid(this.grid.getDim(), this.grid.getSize());

        //voisin est le voisinage relatif de l'automate
        int[][] voisin = this.automate.getVoisinage();

        //pour chaque cellule de la grille
        for(int i = 0 ; i < grid_temp.getLenGrid(); i++){

            int[] etatVoisin = this.grid.getEtatVoisinage(voisin, i);

        }
    }

    public void print() {
        this.grid.print();
    }
}
