import java.io.IOException;

public class Moteur {
    private final Automate automate;
    private final Grid grid;

    public Moteur(String rulesPath,int size) throws IOException {
        this.automate = Automate.fromJson(rulesPath);
        this.grid = new Grid(automate.getDimension(), size);
    }

    public void initGrid(int[][] tab){
        for (int i = 0; i < tab.length; i++) {
            this.grid.setCase(tab[i][0], tab[i][1]);
        }
    }

    public void update() {

        System.out.println("update");
        //grid_temp est la grille temporaire qui va être remplie
        Grid grid_temp = new Grid(this.grid.getDim(), this.grid.getSize());

        //voisin est le voisinage relatif de l'automate
        int[][] voisin = this.automate.getVoisinage();

        //pour chaque cellule de la grille
        for(int i = 0 ; i < grid_temp.getLenGrid(); i++){

            int[] etatVoisinage = this.grid.getEtatVoisinage(voisin, i);
            int etatCase = etatVoisinage[0];
            etatVoisinage[0] = 0;
            int newEtat = this.automate.sigma(etatVoisinage, etatCase);
            grid_temp.setCase(i, newEtat);
        }

        this.grid.setGrid(grid_temp.getGrid());
    }

    public void print() {
        this.grid.print2D();
    }
}
