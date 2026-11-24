package jeu.modele;

public class Rue extends Lieu {

    private float longueur;

    public Rue(String nom, String description, float longueur) {
        super(nom, description);
        this.longueur = longueur;
    }

    public float getLongueur() {
        return longueur;
    }

    public void setLongueur(float longueur) {
        this.longueur = longueur;
    }

    public void traverser() {
        // à implémenter si nécessaire
    }
}