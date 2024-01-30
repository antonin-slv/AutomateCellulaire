package code.src;

public class Main {
    public static void main(String[] args) {
        
        Moteur moteur = new Moteur("JDLV.json");
        
        Grid grille = new Grid(3, 2, new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        System.out.println(grille.getCase(new int[]{1, 1, 0}));
    }
}
