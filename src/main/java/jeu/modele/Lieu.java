package jeu.modele;

import java.util.ArrayList;
import java.util.List;

public class Lieu {
    private String nom;
    private String description;
    private List<Objet> objets = new ArrayList<>();
    private List<PNJ> pnjs = new ArrayList<>();
    private List<Lieu> lieuxAdjacents = new ArrayList<>();

    public Lieu(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public String getNom() { return nom; }
    public String getDescription() { return description; }

    public List<Objet> getObjets() { return objets; }
    public List<PNJ> getPNJs() { return pnjs; }
    public List<Lieu> getLieuxAdjacents() { return lieuxAdjacents; }

    public void ajouterObjet(Objet o) { objets.add(o); }
    public void retirerObjet(Objet o) { objets.remove(o); }

    public void ajouterPNJ(PNJ p) { pnjs.add(p); }

    public void addLink(Lieu l) {
        if (!lieuxAdjacents.contains(l)) {
            lieuxAdjacents.add(l);
        }
    }

    public void ajouterAdjacent(Lieu l) {
        this.addLink(l);
        l.addLink(this);
    }

    @Override
    public String toString() {
        return nom + " : " + description;
    }
}