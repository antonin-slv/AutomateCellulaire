import lombok.Getter;

public class Grid {
    @Getter
    private int dim;
    private int[] grid;
    @Getter
    private int size;
    @Getter
    private boolean cycle = false;

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
        int indice = coordToInt(coords);
        if(indice == -1)
            return 0;

        return grid[indice];
    }

    public void setCase(int[] coords, int value) {
        int indice = coordToInt(coords);
        grid[indice] = value;
    }

    public int getLenGrid(){
        return this.grid.length;
    }

    /**
     * @param coordsVoisinageRelatif : tableau des coordonnées relatives des voisins
     * @param pos : position de la cellule dans la grille (indice int)
     * @return tableau des états des voisins
     */
    public int[] getEtatVoisinage(int[][] coordsVoisinageRelatif, int pos) {

        int[] position = intToCoord(pos);

                            System.out.println("Position : ");
                            for (int i = 0; i < position.length; i++) { /////PRINNNNTTT
                                System.out.print(position[i] + " ");
                            }
                            System.out.println();

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
        int indice = 0;
        for (int i = 0; i < coords.length; i++) {
            if (coords[i] < 0 || coords[i] >= this.size) {
                if(this.cycle){
                    coords[i] = (coords[i] + this.size) % this.size;
                }
                else {
                    return -1;
                }
            }
            indice += (int) (coords[i] * Math.pow(this.size, i));
        }
        return indice;
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

    public void print() {
        for (int i = 0; i < grid.length; i++) {
            System.out.print(grid[i] + " ");
            if((i + 1) % size == 0)
                System.out.println();
        }
    }
}


