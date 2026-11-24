package jeu.modele;

public class Cle extends Objet {
    private String codeAcces;

    public Cle(String nom, float poids, String codeAcces) {
        super(nom, poids);
        this.codeAcces = codeAcces;
    }

    public String getCodeAcces() { return codeAcces; }

    public boolean correspond(Batiment b) {
        return b.getCodeAcces().equals(codeAcces);
    }

    @Override
    public void utiliser(Personnage p) {
        // utilisation gérée via le contrôleur sur un bâtiment
    }
}