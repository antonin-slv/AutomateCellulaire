// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Grid grille = new Grid(3, 2, new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        System.out.println(grille.getCase(new int[]{1, 1, 0}));
    }
}
