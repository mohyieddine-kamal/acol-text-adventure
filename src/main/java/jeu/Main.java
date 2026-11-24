package jeu;

import jeu.controleur.JeuControleur;
import jeu.vue.IHMTextuelle;

public class Main {
    public static void main(String[] args) {
        IHMTextuelle vue = new IHMTextuelle();
        JeuControleur controleur = new JeuControleur(vue);
        controleur.boucleJeu();
    }
}