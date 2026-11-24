package jeu.modele;

import java.util.ArrayList;
import java.util.List;

public class Ville {
    private String nom;
    private List<Lieu> lieux = new ArrayList<>();

    public Ville(String nom) {
        this.nom = nom;
    }

    public String getNom() { return nom; }
    public List<Lieu> getLieux() { return lieux; }

    public void ajouterLieu(Lieu l) {
        lieux.add(l);
    }
}