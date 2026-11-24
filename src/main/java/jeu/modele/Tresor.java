package jeu.modele;

public class Tresor extends Objet {
    private int valeur;
    private String description;

    public Tresor(String nom, float poids, int valeur, String description) {
        super(nom, poids);
        this.valeur = valeur;
        this.description = description;
    }

    public int getValeur() { return valeur; }
    public String getDescriptionTresor() { return description; }

    public void decouvrir() {
        // marque la victoire, géré dans le contrôleur
    }

    @Override
    public void utiliser(Personnage p) {
        // rien, la découverte suffit
    }
}