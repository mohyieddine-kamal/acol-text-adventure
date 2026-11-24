package jeu.modele;

import java.util.Random;

public class PNJ extends Personnage {
    private boolean aJoue;
    private String dialogue;

    public PNJ(String nom, int pointsVie, Lieu position, String dialogue) {
        super(nom, pointsVie, position);
        this.aJoue = false;
        this.dialogue = dialogue;
    }

    public String getDialogue() {
        return dialogue;
    }


    public boolean aJoue() {
        return aJoue;
    }

    public void setAJoue(boolean aJoue) {
        this.aJoue = aJoue;
    }

    public String jouerPierreFeuilleCiseaux(String choixJoueur, StringBuilder pnjChoice) {
        String[] options = {"pierre", "feuille", "ciseaux"};
        String choixPNJ = options[new Random().nextInt(options.length)];
        pnjChoice.append(choixPNJ);

        if (choixJoueur.equals(choixPNJ)) {
            return "draw";
        } else if ((choixJoueur.equals("pierre") && choixPNJ.equals("ciseaux")) ||
                   (choixJoueur.equals("feuille") && choixPNJ.equals("pierre")) ||
                   (choixJoueur.equals("ciseaux") && choixPNJ.equals("feuille"))) {
            return "win";
        } else {
            return "lose";
        }
    }
}