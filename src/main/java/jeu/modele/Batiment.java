package jeu.modele;

public class Batiment extends Lieu {
    private boolean estVerrouille;
    private String codeAcces;
    private Lieu interieur;

    public Batiment(String nom, String description, boolean estVerrouille, String codeAcces) {
        super(nom, description);
        this.estVerrouille = estVerrouille;
        this.codeAcces = codeAcces;
    }

    public void ajouterInterieur(Lieu l) {
        this.interieur = l;
        if (l != null) {
            l.addLink(this); // Permet de sortir
        }
    }

    public Lieu getInterieur() {
        return interieur;
    }

    public boolean isEstVerrouille() { return estVerrouille; }
    public String getCodeAcces() { return codeAcces; }

    public boolean deverrouiller(Cle c) {
        if (c.getCodeAcces().equals(codeAcces)) {
            estVerrouille = false;
            return true;
        }
        return false;
    }

    public boolean entrer(Personnage p) {
        if (!estVerrouille) {
            if (interieur != null) {
                p.seDeplacer(interieur);
            } else {
                p.seDeplacer(this);
            }
            return true;
        }
        return false;
    }
}