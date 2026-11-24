package jeu.controleur;

public class CommandeParser {

    public static String[] parse(String ligne) {
        if (ligne == null) return new String[0];
        return ligne.trim().split("\\s+");
    }
}