public class Grid {
    private int dim;
    private int[] grid;
    private int size;

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
        int indice = coordToInt(coords, size);
        return grid[indice];
    }

    public void setCase(int[] coords, int value) {
        int indice = coordToInt(coords, size);
        grid[indice] = value;
    }

    public int getLenGrid(){
        return this.grid.length;
    }
    
    public int[] getEtatVoisinage(int[][] coordsVoisinage) {
        int[] etatVoisinage = new int[coordsVoisinage.length];
        for (int i = 0; i < coordsVoisinage.length; i++) {
            etatVoisinage[i] = getCase(coordsVoisinage[i]);
        }
        return etatVoisinage;
    }

    public static int coordToInt(int[] coords, int taille) {
        int indice = 0;
        for (int i = 0; i < coords.length; i++) {
            if (coords[i] < 0 || coords[i] >= taille) {
                throw new IllegalArgumentException("Les coordonnées sont hors de la grille");
            }
            indice += (int) (coords[i] * Math.pow(taille, i));
        }
        return indice;
    }
}


