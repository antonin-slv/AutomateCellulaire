package core;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * This class is the Engine of the program
 * <br />
 *  It is the link between automaton and Grids
 * @see Automate
 * @see Grid
 */
@Getter
@Setter
public class Moteur {

    /**
     * automaton used by the engine
     * @see Automate
     */
    private final Automate automate;
    /**
     * Grid used by the engine
     * @see Grid
     */
    @Setter
    private Grid grid;

    /**
     * Path of the json file containing the definition of the automaton
     */
    @Getter
    private String rulesPath;


    /**
     * Constructor of the Moteur class
     * @param rulesPath path of the json file containing the definition of the automaton
     * @param size size of the grid (max(index)+1 for a given coordinate)
     * @throws IOException if there is an error while reading the file
     */
    public Moteur(String rulesPath,int size) throws IOException {
        this.rulesPath = rulesPath;
        this.automate = Automate.fromJson(rulesPath);
        this.grid = new Grid(automate.getDimension(), size);
    }

    /**
     * Constructor of the Moteur class
     * @param rulesPath path of the json file containing the definition of the automaton
     * @param gridPath path of the json file containing the declaration of the Grid
     * @throws IOException if there is an error while reading a file
     */
    public Moteur(String rulesPath, String gridPath) throws IOException {
        this.automate = Automate.fromJson(rulesPath);
        this.grid = Grid.fromJson(gridPath);
    }

    /**
     * Function that initializes the grid with the given tab
     * @param tab Arrray of (index,value) where index is in the 1D array of state of the grid
     */
    public void initGrid(int[][] tab){
        for (int[] ints : tab) {
            this.grid.setCase(ints[0], ints[1]);
        }
    }

    /**
     * Saves the state of the grid in a json file
     * @param saveName name of the file where the grid will be saved
     * @throws IOException if there is an error while writing the file
     */
    public void saveGrid(String saveName) throws IOException {
        this.grid.toJson(saveName);
    }

    /**
     * Function that updates the grid according to the rules of the automaton
     */
    public void update() {

        //grid_temp est la grille temporaire qui va être remplie
        Grid grid_temp = new Grid(this.grid.getDim(), this.grid.getSize());

        //voisin est le voisinage relatif de l'automate
        int[][] voisin = this.automate.getVoisinage();

        //pour chaque cellule de la grille
        for(int i = 0 ; i < grid_temp.getLenGrid(); i++){
            int[] etatVoisinage = this.grid.getEtatVoisinage(voisin, i);
            int newEtat = this.automate.applyRule(etatVoisinage);
            grid_temp.setCase(i, newEtat);
        }

        this.grid.setTabGrid(grid_temp.getTabGrid());
    }


    /**
     * Function that returns the state of a cell
     * @param coords coordinates of the cell (N-uplet)
     * @return state of the cell
     */
    public int getEtat(int[] coords){
        return this.grid.getCase(coords);
    }

    /**
     * Function that returns the rules of the automaton
     * @return rules of the automaton in a String
     */
    public String getRules(){
        return this.automate.getRegle().toString();
    }


    /**
     * Function that sets the state of a cell
     * @param coords coordinates of the cell (N-uplet)
     * @param value new state of the cell
     */
    public void setEtat(int[] coords, int value){
        this.grid.setCase(coords, value);
    }

    /**
     * Function that print the grid in the console
     */
    public void print() {
        this.grid.print2D();
    }

}
