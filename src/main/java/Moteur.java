import java.io.IOException;

public class Moteur {
    private Automate automate;
    private Grid grid;

    public Moteur(String rulesPath) throws IOException {

        this.automate = Automate.fromJson(rulesPath);
        this.grid = new Grid(automate.getDim(), 20);
    }

    public void update() {
        //Todo : update the grid
        
        Grid grid_temp = new Grid(this.grid.getDim(), this.grid.getSize());
        int[][] voisin = this.automate.getCoordsVoisinage();
        for(int i = 0 ; i < grid_temp.getLenGrid(); i++){
            // on a les voisin et le numero de la case à évaluer
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void print() {
        //Todo : print the grid
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
