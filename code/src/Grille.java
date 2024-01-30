public class Grille {
    private int dim;
    private int[] grille;
    private int taille;

    public Grille(int dim, int taille) {
        this.dim = dim;
        this.taille = taille;
        this.grille = new int[(int) Math.pow(taille, dim)];
    }
    public int getCase(int[] coords) {
        //Todo : get the value of the case at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setCase(int[] coords, int value) {
        //Todo : set the value of the case at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int[] getEtatVoisinage(int[] coordsVoisinage) {
        //Todo : get the neighborhood of the case at the given coordinates
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
