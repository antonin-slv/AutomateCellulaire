import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Grid {
    private final int dim;
    private int[] grid;
    private final int size;
    private final boolean cycle = true;

    public Grid(int dim, int size) {
        this.dim = dim;
        this.size = size;
        this.grid = new int[(int) Math.pow(size, dim)];
    }

    public Grid(int dim, int size, int[] grid) {
        this.dim = dim;
        this.size = size;
        this.grid = grid;
    }

    public int getCase(int[] coords) {
        int index = coordToInt(coords);
        if(index == -1)
            return 0;
        
        return grid[index];
    }

    public void setCase(int[] coords, int value) {
        int index = coordToInt(coords);
        grid[index] = value;
    }

    public void setCase(int index, int value) {
        grid[index] = value;
    }

    public int getLenGrid(){
        return this.grid.length;
    }

    /**
     * @param coordsVoisinageRelatif : tableau des coordonnées relatives des voisins
     * @param pos : position de la cellule dans la grille (index int)
     * @return tableau des états des voisins
     */
    public int[] getEtatVoisinage(int[][] coordsVoisinageRelatif, int pos) {

        int[] position = intToCoord(pos);
        int[][] coordsVoisinage = new int[coordsVoisinageRelatif.length][coordsVoisinageRelatif[0].length];

        for (int i = 0; i < coordsVoisinageRelatif.length; i++) {
            for (int j = 0; j < coordsVoisinageRelatif[i].length; j++) {
                coordsVoisinage[i][j] = position[j] + coordsVoisinageRelatif[i][j];
            }
        }

        int[] etatVoisinage = new int[coordsVoisinage.length];
        for (int i = 0; i < coordsVoisinage.length; i++) {
            etatVoisinage[i] = getCase(coordsVoisinage[i]);
        }

        return etatVoisinage;
    }

    public int coordToInt(int[] coords) {
        int index = 0;
        for (int i = 0; i < coords.length; i++) {
            if (coords[i] < 0 || coords[i] >= this.size) {
                if(this.cycle){
                    coords[i] = (coords[i] + this.size) % this.size;
                }
                else {
                    return -1;
                }
            }
            index += (int) (coords[i] * Math.pow(this.size,coords.length -1 -i));
        }
        return index;
    }

    public  int [] intToCoord(int number) {
        int [] truc = new int [this.dim];
        int rest;
        for (int i = this.dim - 1; i >= 0; i --) {
            rest = number % this.size;
            number -= rest;
            number /= this.size;
            truc [i] = rest;
        }
        return truc;
    }

    public void print2D() {
        for (int i = 0; i < grid.length; i++) {
            System.out.print(grid[i] + " ");
            if((i + 1) % size == 0)
                System.out.println();
        }
        System.out.println();
    }

    /**
     * Print the grid in a 2D hexagonal shape
     */
    public void printHexa2D(){
        for (int i = 0; i < grid.length; i++) {
            System.out.print(grid[i] + " ");

        }
        System.out.println();

    }
}


