package jeu.modele;

public class Livre extends Objet {
    private String auteur;

    public Livre(String nom, float poids, String auteur) {
        super(nom, poids);
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    @Override
    public void utiliser(Personnage p) {
        // Pour l'instant, on ne fait rien avec le livre
    }
}
