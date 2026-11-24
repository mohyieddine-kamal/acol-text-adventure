package jeu.modele;

import java.util.ArrayList;
import java.util.List;

public class Personnage {
    protected String nom;
    protected int pointsVie;
    protected Lieu position;
    protected List<Objet> sac = new ArrayList<>();

    public Personnage(String nom, int pointsVie, Lieu position) {
        this.nom = nom;
        this.pointsVie = pointsVie;
        this.position = position;
    }

    public String getNom() { return nom; }
    public int getPointsVie() { return pointsVie; }
    public Lieu getPosition() { return position; }
    public List<Objet> getSac() { return sac; }

    public void seDeplacer(Lieu l) {
        this.position = l;
    }

    public void ramasser(Objet o) {
        sac.add(o);
    }

    public void utiliserObjet(Objet o) {
        o.utiliser(this);
    }

    public void subirDegats(int d) {
        pointsVie -= d;
    }

    public boolean estMort() {
        return pointsVie <= 0;
    }
}