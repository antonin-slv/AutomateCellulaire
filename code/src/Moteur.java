public class Moteur {
    private Automate automate;
    private Grille grille;

    public Moteur(String rules_path) {

        this.automate = new Automate(rules_path);
        this.grille = new Grille(automate.getDim(), 20);
    }

    public void update() {
        //Todo : update the grid
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void print() {
        //Todo : print the grid
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
