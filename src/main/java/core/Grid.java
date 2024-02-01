package core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

@Getter
@Setter
public class Grid {
    ///dim : dimension de la grille (1D, 2D, 3D, ...)
    private final int dim;
    ///grid : tableau des états des cellules
    private int[] grid;
    ///size : taille de la grille (taille d'un côté)
    private final int size;
    ///cycle : true si la grille est cyclique, false sinon
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


    public static Grid fromJson(String gridPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(gridPath))) {
            JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
            int dim = json.get("dim").getAsInt();
            int size = json.get("size").getAsInt();
            JsonArray grid = json.get("grid").getAsJsonArray();

            Stream<JsonElement> stream = grid.asList().stream();

            if (json.get("save") != null) {
                int[] finalGrid = stream.mapToInt(JsonElement::getAsInt).toArray();
                if (finalGrid.length != Math.pow(size, dim)) {
                    throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
                }

                return new Grid(dim, size, finalGrid);
            }

            for (int i = 0; i < dim - 1; i++) {
                stream = stream.flatMap(e -> e.getAsJsonArray().asList().stream());
            }

            int[] finalGrid = stream.mapToInt(JsonElement::getAsInt).toArray();

            if (finalGrid.length != Math.pow(size, dim)) {
                throw new UnsupportedOperationException("La taille de la grille ne correspond pas à la dimension");
            }

            return new Grid(dim, size, finalGrid);
        }
    }

    public void randomize(int alphabetSize) {
        for (int i = 0; i < grid.length; i++) {
            grid[i] = (int) (Math.random() * alphabetSize);
        }
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
        if(this.dim == 2) {
            for (int i = 0; i < grid.length; i++) {
            System.out.print(grid[i] + " ");
            if((i + 1) % size == 0)
                System.out.println();
            }
        }
        else if(this.dim == 1) {

            for (int j : grid) {
                if (j == 0){
                    System.out.print(" ");
                }
                else
                    System.out.print(j);
            }
            System.out.println();
        }
        else
            System.out.println("Dimension non prise en charge");


        if(this.dim == 1)
            return;
        System.out.println();
    }

    /**
     * Print the grid in a 2D hexagonal shape
     */
    public void printHexa2D(){
        for (int i = 0; i < this.size; i++) {
            if(i % 2 == 1)
                System.out.print(" ");

            for(int j = this.size - (i/2) ; j < this.size; j++){
                System.out.print(this.grid[i * this.size + j] + " ");
            }
            for (int j = 0; j < this.size - (i/2) ;j++){
                System.out.print(this.grid[i * this.size + j] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }
}


