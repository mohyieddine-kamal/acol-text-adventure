package jeu.vue;

import java.util.Scanner;

public class IHMTextuelle {

    private Scanner scanner = new Scanner(System.in);

    public void afficherMessage(String msg) {
        System.out.println(msg);
    }

    public String lireCommande() {
        System.out.print("> ");
        // Si l'entrée se termine (Ctrl+D, flux redirigé qui s'arrête) sans
        // que le joueur ait tapé "quit", il n'y a plus de ligne à lire :
        // on traite ça comme une sortie normale plutôt que de laisser
        // planter le jeu avec une NoSuchElementException.
        if (!scanner.hasNextLine()) {
            return "quit";
        }
        return scanner.nextLine();
    }
}