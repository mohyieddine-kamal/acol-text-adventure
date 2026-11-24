package jeu.modele;

public class Arme extends Objet {
    private int degats;

    public Arme(String nom, float poids, int degats) {
        super(nom, poids);
        this.degats = degats;
    }

    public int getDegats() { return degats; }

    public void attaquer(PNJ cible) {
        cible.subirDegats(degats);
    }

    @Override
    public void utiliser(Personnage p) {
        // par défaut: ne fait rien spécial sur le joueur
    }
}