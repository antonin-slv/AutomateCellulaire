package core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gui.MapCreatorController;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;


/**
 * Class that represents a Grid.
 * <br />
 * This class has a 1D array that can be accessed as an N-D array.
 * It stores the states of the cells for an automaton.
 * Those states are represented by integers.
 * <br />
 * the access method for out of bounds (index>=size) neighbors is cyclic by default
 */
@Getter
@Setter
public class Grid {
    /**
     * dimension of the grid, from 1 to N Dimensions
     */
    private final int dim;
    /**
     * array of the states of each cell, represented by Integers
     */
    private int[] tabGrid;
    /**
     * size : max index that can be reached on each dimension
     */
    private final int size;
    /**
     * cycle : true if grid is toroidal (cyclic), false otherwise
     */
    private final boolean cycle = true;

    /**
     * Random object used to randomize the states of the cells
     */
    private final transient Random rand = new Random();


    /**
     * Constructor of the Grid class
     *
     * @param dim  dimension of the grid
     * @param size size of the grid (max(index)+1 for a given coordinate)
     */
    public Grid(int dim, int size) {
        this.dim = dim;
        this.size = size;
        this.tabGrid = new int[(int) Math.pow(size, dim)];
        Arrays.fill(this.tabGrid, 0);
    }


    /**
     * Constructor of the Grid class
     *
     * @param dim     dimension of the grid
     * @param size    size of the grid (max(index)+1 for a given coordinate)
     * @param tabGrid tab of the states of the cells in 1D (tabGrid.length = size^dim)
     */
    public Grid(int dim, int size, int[] tabGrid) {
        this.dim = dim;
        this.size = size;
        this.tabGrid = tabGrid;
    }


    /**
     * Function that creates a Grid object from a json file containing its definition
     *
     * @param gridPath path of the json file containing the definition of the Grid
     * @return The given Grid
     * @throws IOException if there is an error while reading the file
     */

    public static Grid fromJson(String gridPath) throws IOException {
        MapCreatorController.bad_load = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(gridPath))) {
            JsonObject json = Main.GSON.fromJson(reader, JsonObject.class);
            int dim = json.get("dim").getAsInt();

            if (dim != Main.getDimension()) {
                MapCreatorController.bad_load = true;
                return Main.getMoteur().getGrid();
            }

            int size = json.get("size").getAsInt();
            JsonArray grid = json.get("tabGrid").getAsJsonArray();

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


    /**
     * Function that saves the configuration of the Grid in a json file
     *
     * @param saveName name of the json file where the configuration will be saved
     * @throws IOException if there is an error while writing the file
     */
    public void toJson(String saveName) throws IOException {
        JsonObject json = Main.GSON.toJsonTree(this).getAsJsonObject();
        json.addProperty("save", Instant.now().toString());

        String fileName = saveName == null ? "save" + Instant.now().toEpochMilli() + ".json" : saveName + ".json";
        File directory = new File("save");
        directory.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/" + fileName, false))) {
            Main.GSON.toJson(json, writer);
        }
    }


    /**
     * Function that randomizes the states of the cells
     * <br />
     * do not use for continuous  rules
     *
     * @param alphabetSize size of the alphabet of the automaton
     */
    public void randomize(int alphabetSize) {
        for (int i = 0; i < tabGrid.length; i++) {
            tabGrid[i] = rand.nextInt(alphabetSize);
        }
    }

    /**
     * Function that get the state of a cell
     *
     * @param coords coordinates of the cell
     * @return state of the cell
     */
    public int getCase(int[] coords) {
        int index = coordToInt(coords);
        if (index == -1)
            return 0;

        return tabGrid[index];
    }

    /**
     * Function that say if the grid is empty
     *
     * @return true if the grid is empty, false otherwise
     */
    public boolean isEmpty() {
        for (int i = 0; i < tabGrid.length; i++) {
            if (tabGrid[i] != 0)
                return false;
        }
        return true;
    }

    /**
     * Function that set the state of a cell given its coordinates
     *
     * @param coords coordinates of the cell
     * @param value  new state of the cell
     */
    public void setCase(int[] coords, int value) {
        int index = coordToInt(coords);
        tabGrid[index] = value;
    }

    /**
     * Function that set the state of a cell
     * <br />
     * It uses directly the index of the cell in the 1D array of the grid.
     *
     * @param index index of the cell
     * @param value new state of the cell
     */
    public void setCase(int index, int value) {
        tabGrid[index] = value;
    }

    /**
     * Function that get the total lenght of the grid
     * It is equal to the size of the grid to the power of its dimension
     *
     * @return total lenght of the grid
     */
    public int getLenGrid() {
        return this.tabGrid.length;
    }

    /**
     * Function that get the stats of the cells arround a given cell
     *
     * @param coordsVoisinageRelatif array of the relative coordinates of the neighbors
     * @param pos                    position of the cell
     * @return array of the states of the neighbors
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

    /**
     * Function that converts the coordinates of a cell to its index in the 1D array of the grid
     *
     * @param coords coordinates of the cell in N dimensions
     * @return index of the cell in the 1D array of the grid
     */
    public int coordToInt(int[] coords) {
        int index = 0;
        int power = 1;
        for (int i = coords.length - 1; i >= 0; i--) {
            if (coords[i] < 0 || coords[i] >= this.size) {
                if (this.cycle) {
                    coords[i] = (coords[i] + this.size) % this.size;
                } else {
                    return -1;
                }
            }
            index += coords[i] * power;
            power *= this.size;
        }
        return index;
    }

    /**
     * Function that converts the index of a cell in the 1D array of the grid to its coordinates
     *
     * @param number index of the cell in the 1D array of the grid
     * @return coordinates of the cell in N dimensions
     */
    public int[] intToCoord(int number) {
        int[] truc = new int[this.dim];
        int rest;
        for (int i = this.dim - 1; i >= 0; i--) {
            rest = number % this.size;
            number -= rest;
            number /= this.size;
            truc[i] = rest;
        }
        return truc;
    }

    /**
     * Function that prints the grid in the console
     */
    public void print2D() {
        if (this.dim == 2) {
            for (int i = 0; i < tabGrid.length; i++) {
                System.out.print(tabGrid[i] + " ");
                if ((i + 1) % size == 0)
                    System.out.println();
            }
        } else if (this.dim == 1) {

            for (int j : tabGrid) {
                if (j == 0) {
                    System.out.print(" ");
                } else
                    System.out.print(j);
            }
            System.out.println();
        } else
            System.out.println("Dimension non prise en charge");


        if (this.dim == 1)
            return;
        System.out.println();
    }

    /**
     * Print the grid in a 2D hexagonal shape
     */
    public void printHexa2D() {
        for (int i = 0; i < this.size; i++) {
            if (i % 2 == 1)
                System.out.print(" ");

            for (int j = this.size - (i / 2); j < this.size; j++) {
                System.out.print(this.tabGrid[i * this.size + j] + " ");
            }
            for (int j = 0; j < this.size - (i / 2); j++) {
                System.out.print(this.tabGrid[i * this.size + j] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    /**
     * Function that replaces a state by another in the grid
     *
     * @param value    state to be replaced
     * @param newValue new state
     */
    public void replace(int value, int newValue) {
        for (int i = 0; i < this.tabGrid.length; i++) {
            if (this.tabGrid[i] == value)
                this.tabGrid[i] = newValue;
        }
    }

    /**
     * Function that cap the maximum state of the grid
     * Will replace all states greater than the maximum by a random state between 0 and the maximum (not included)
     *
     * @param max new maximum state
     */
    public void setGridMaxEtat(int max) {
        for (int i = 0; i < this.tabGrid.length; i++) {
            if (this.tabGrid[i] >= max)
                this.tabGrid[i] = this.rand.nextInt(max);
        }
    }
}


