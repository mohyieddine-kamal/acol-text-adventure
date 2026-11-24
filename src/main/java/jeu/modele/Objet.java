package jeu.modele;

public abstract class Objet {
    protected String nom;
    protected float poids;

    public Objet(String nom, float poids) {
        this.nom = nom;
        this.poids = poids;
    }

    public String getNom() { return nom; }
    public float getPoids() { return poids; }

    public abstract void utiliser(Personnage p);
}