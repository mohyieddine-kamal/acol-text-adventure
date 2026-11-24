package jeu.controleur;

import jeu.modele.*;
import jeu.vue.IHMTextuelle;

import java.util.Optional;

public class JeuControleur {

    private Ville ville;
    private Joueur joueur;
    private IHMTextuelle vue;
    private boolean termine = false;
    private Tresor tresorFinal;

    public JeuControleur(IHMTextuelle vue) {
        this.vue = vue;
        initialiserMonde();
    }

    private void initialiserMonde() {
        ville = new Ville("VilleMysterieuse");

        // --- Lieux Exterieurs ---
        Lieu place = new Lieu("Place", "Une grande place centrale.");
        Lieu rue = new Lieu("Rue", "Une rue sombre.");
        Batiment maison = new Batiment("Maison", "Une petite maison verrouillée.", true, "CODE123");
        Batiment armurerie = new Batiment("Armurerie", "Un endroit pour forger des armes.", false, null);
        Batiment bibliotheque = new Batiment("Bibliotheque", "Un lieu de savoir.", false, null);
        
        place.ajouterAdjacent(rue);
        place.ajouterAdjacent(armurerie);
        place.ajouterAdjacent(bibliotheque);
        rue.ajouterAdjacent(maison);

        // --- Interieur de la Maison ---
        Lieu salon = new Lieu("Salon", "Un salon confortable.");
        Lieu chambre = new Lieu("Chambre", "Une chambre simple.");
        maison.ajouterInterieur(salon);
        salon.ajouterAdjacent(chambre);

        // --- Interieur de l'Armurerie ---
        Lieu forge = new Lieu("Forge", "La forge du forgeron.");
        armurerie.ajouterInterieur(forge);

        // --- Interieur de la Bibliotheque ---
        Lieu salleLecture = new Lieu("Salle_de_Lecture", "Une salle calme pour lire.");
        bibliotheque.ajouterInterieur(salleLecture);

        // --- PNJs ---
        PNJ gardien = new PNJ("Gardien", 100, chambre, "Bonjour, aventurier. Si vous voulez le trésor, il faudra me battre à pierre-feuille-ciseaux !");
        chambre.ajouterPNJ(gardien);
        PNJ forgeron = new PNJ("Forgeron", 100, forge, "Bienvenue dans ma forge. Je peux vous fabriquer les meilleures armes de la ville.");
        forge.ajouterPNJ(forgeron);
        PNJ bibliothecaire = new PNJ("Bibliothecaire", 100, salleLecture, "Chut ! Vous êtes dans une bibliothèque. Respectez le silence.");
        salleLecture.ajouterPNJ(bibliothecaire);

        // --- Objets ---
        Cle cleMaison = new Cle("cle_maison", 0.1f, "CODE123");
        place.ajouterObjet(cleMaison);
        tresorFinal = new Tresor("Tresor", 1.0f, 1000, "Un coffre rempli d'or.");
        chambre.ajouterObjet(tresorFinal);

        Livre livre1 = new Livre("Le_Seigneur_des_Anneaux", 1.5f, "J.R.R. Tolkien");
        Livre livre2 = new Livre("Java_pour_les_Nuls", 1.0f, "Barry Burd");
        bibliothecaire.ramasser(livre1);
        bibliothecaire.ramasser(livre2);

        // --- Ajout des lieux a la ville ---
        ville.ajouterLieu(place);
        ville.ajouterLieu(rue);
        ville.ajouterLieu(maison);
        ville.ajouterLieu(armurerie);
        ville.ajouterLieu(bibliotheque);
        ville.ajouterLieu(salon);
        ville.ajouterLieu(chambre);
        ville.ajouterLieu(forge);
        ville.ajouterLieu(salleLecture);

        joueur = new Joueur("Heros", 100, place);
    }

    public void boucleJeu() {
        vue.afficherMessage("Bienvenue dans le jeu d'aventures !");
        vue.afficherMessage("Tapez 'menu' pour voir les commandes disponibles.");
        afficherLieuCourant();

        while (!termine) {
            String ligne = vue.lireCommande();
            traiterCommande(ligne);

            if (joueur.estMort()) {
                vue.afficherMessage("Vous êtes mort. Partie terminée.");
                termine = true;
            }
        }
    }

    private void afficherLieuCourant() {
        Lieu lieu = joueur.getPosition();
        vue.afficherMessage("Vous êtes à " + lieu.getNom());
        vue.afficherMessage(lieu.getDescription());
        if (!lieu.getObjets().isEmpty()) {
            vue.afficherMessage("Objets ici : ");
            lieu.getObjets().forEach(o -> vue.afficherMessage(" - " + o.getNom()));
        }
        if (!lieu.getPNJs().isEmpty()) {
            vue.afficherMessage("Personnages ici : ");
            lieu.getPNJs().forEach(p -> vue.afficherMessage(" - " + p.getNom()));
        }
    }

    private void traiterCommande(String ligne) {
        String[] tokens = CommandeParser.parse(ligne);
        if (tokens.length == 0 || tokens[0].isEmpty()) return;

        String cmd = tokens[0].toLowerCase();

        switch (cmd) {
            case "quit":
            case "exit":
                termine = true;
                vue.afficherMessage("Au revoir !");
                break;
            case "look":
                afficherLieuCourant();
                break;
            case "go":
                if (tokens.length >= 2) {
                    deplacerVers(tokens[1]);
                } else {
                    vue.afficherMessage("Usage : go <nomLieu>");
                }
                break;
            case "take":
                if (tokens.length >= 2) {
                    ramasserObjet(tokens[1]);
                } else {
                    vue.afficherMessage("Usage : take <nomObjet>");
                }
                break;
            case "inventory":
                afficherSac();
                break;
            case "enter":
                entrerBatiment();
                break;
            case "menu":
            	afficherMenu();
            	break;
            case "map":
            	afficherCarte();
            	break;
            case "play":
                if (tokens.length >= 2) {
                    jouerPartie(tokens[1]);
                } else {
                    vue.afficherMessage("Usage : play <pierre|feuille|ciseaux>");
                }
                break;
            case "talk":
                if (tokens.length >= 2) {
                    parlerAuPNJ(tokens[1]);
                } else {
                    vue.afficherMessage("Usage : talk <nom du personnage>");
                }
                break;
            case "ask":
                if (tokens.length >= 2) {
                    demanderLivre(tokens[1]);
                } else {
                    vue.afficherMessage("Usage : ask <nom_du_livre>");
                }
                break;
            default:
                vue.afficherMessage("Commande inconnue.");
        }
    }

    private void deplacerVers(String nomLieu) {
        Lieu courant = joueur.getPosition();
        Optional<Lieu> dest = courant.getLieuxAdjacents().stream()
                .filter(l -> l.getNom().equalsIgnoreCase(nomLieu))
                .findFirst();
        if (dest.isPresent()) {
            joueur.seDeplacer(dest.get());
            afficherLieuCourant();
        } else {
            vue.afficherMessage("Vous ne pouvez pas aller là depuis ici.");
        }
    }

    private void ramasserObjet(String nomObjet) {
        Lieu courant = joueur.getPosition();
        Optional<Objet> objOpt = courant.getObjets().stream()
                .filter(o -> o.getNom().equalsIgnoreCase(nomObjet))
                .findFirst();
        
        if (objOpt.isPresent()) {
            Objet o = objOpt.get();

            if (o == tresorFinal) {
                Optional<PNJ> gardienOpt = courant.getPNJs().stream()
                        .filter(p -> p.getNom().equalsIgnoreCase("Gardien"))
                        .findFirst();

                if (gardienOpt.isPresent() && !gardienOpt.get().aJoue()) {
                    vue.afficherMessage("Vous devez d'abord gagner contre le gardien ! Utilisez la commande 'play'.");
                    return;
                }
            }

            joueur.ramasser(o);
            courant.retirerObjet(o);
            vue.afficherMessage("Vous ramassez : " + o.getNom());
            if (o == tresorFinal) {
                vue.afficherMessage("Vous avez trouvé le trésor ! Victoire !");
                termine = true;
            }
        } else {
            vue.afficherMessage("Cet objet n'est pas ici.");
        }
    }

    private void afficherSac() {
        if (joueur.getSac().isEmpty()) {
            vue.afficherMessage("Votre sac est vide.");
        } else {
            vue.afficherMessage("Votre sac contient :");
            joueur.getSac().forEach(o -> vue.afficherMessage(" - " + o.getNom()));
        }
    }

    private void entrerBatiment() {
        Lieu courant = joueur.getPosition();
        if (!(courant instanceof Batiment)) {
            vue.afficherMessage("Vous n'êtes pas devant un bâtiment à entrer directement.");
            return;
        }
        Batiment b = (Batiment) courant;
        if (!b.isEstVerrouille()) {
            if (b.getInterieur() != null) {
                joueur.seDeplacer(b.getInterieur());
                afficherLieuCourant();
                if (b.getNom().equalsIgnoreCase("Bibliotheque")) {
                    vue.afficherMessage("\n[Bibliothecaire] : Bonjour. Souhaitez-vous un livre ou préférez-vous partir ?");
                    vue.afficherMessage("                   (répondez avec 'ask <nom_du_livre>' ou 'go Place')");
                }
            } else {
                vue.afficherMessage("Il n'y a rien de plus à explorer dans ce bâtiment.");
            }
            return;
        }

        for (Objet o : joueur.getSac()) {
            if (o instanceof Cle) {
                Cle c = (Cle) o;
                if (b.deverrouiller(c)) {
                    vue.afficherMessage("Vous déverrouillez le bâtiment avec " + c.getNom());
                    if (b.entrer(joueur)) {
                        afficherLieuCourant();
                    }
                    return;
                }
            }
        }
        vue.afficherMessage("Vous n'avez pas la clé pour ce bâtiment.");
    }
    
    private void jouerPartie(String choix) {
        Lieu courant = joueur.getPosition();
        Optional<PNJ> gardienOpt = courant.getPNJs().stream()
                .filter(p -> p.getNom().equalsIgnoreCase("Gardien"))
                .findFirst();

        if (gardienOpt.isEmpty()) {
            vue.afficherMessage("Le gardien n'est pas ici pour jouer.");
            return;
        }

        PNJ gardien = gardienOpt.get();
        if (gardien.aJoue()) {
            vue.afficherMessage("Vous avez déjà gagné contre le Gardien. Le trésor est à vous !");
            return;
        }

        if (!choix.equals("pierre") && !choix.equals("feuille") && !choix.equals("ciseaux")) {
            vue.afficherMessage("Choix invalide. Veuillez choisir pierre, feuille, ou ciseaux.");
            return;
        }

        StringBuilder pnjChoice = new StringBuilder();
        String resultat = gardien.jouerPierreFeuilleCiseaux(choix, pnjChoice);
        vue.afficherMessage("Le Gardien a choisi : " + pnjChoice.toString());

        switch (resultat) {
            case "win":
                vue.afficherMessage("Vous avez gagné ! Vous pouvez maintenant prendre le trésor.");
                gardien.setAJoue(true);
                break;
            case "lose":
                vue.afficherMessage("Vous avez perdu ! Tentez votre chance à nouveau ou quittez la pièce.");
                vue.afficherMessage("Pour rejouer : play <pierre|feuille|ciseaux>");
                vue.afficherMessage("Pour partir : go Salon");
                break;
            case "draw":
                vue.afficherMessage("Egalité ! Essayez encore.");
                break;
        }
    }

    private void parlerAuPNJ(String nomPNJ) {
        Lieu courant = joueur.getPosition();
        Optional<PNJ> pnjOpt = courant.getPNJs().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nomPNJ))
                .findFirst();

        if (pnjOpt.isPresent()) {
            PNJ pnj = pnjOpt.get();
            vue.afficherMessage("[" + pnj.getNom() + "] : " + pnj.getDialogue());
        } else {
            vue.afficherMessage("Il n'y a personne de ce nom ici.");
        }
    }

    private void afficherMenu() {
        vue.afficherMessage("Commandes disponibles :");
        vue.afficherMessage(" - look : regarder autour de vous");
        vue.afficherMessage(" - go <lieu> : vous déplacer vers un lieu");
        vue.afficherMessage(" - take <objet> : ramasser un objet");
        vue.afficherMessage(" - inventory : voir votre inventaire");
        vue.afficherMessage(" - enter : entrer dans un bâtiment");
        vue.afficherMessage(" - menu : afficher cette aide");
        vue.afficherMessage(" - map : afficher la carte");
        vue.afficherMessage(" - play <pierre|feuille|ciseaux> : jouer contre un PNJ");
        vue.afficherMessage(" - talk <nom du personnage> : parler à un personnage");
        vue.afficherMessage(" - ask <nom_du_livre> : demander un livre au bibliothécaire");
        vue.afficherMessage(" - quit/exit : quitter le jeu");
    }

    private void afficherCarte() {
        Lieu lieuActuel = joueur.getPosition();
        vue.afficherMessage("Carte :");
        vue.afficherMessage("Vous êtes ici : [" + lieuActuel.getNom() + "]");
        for (Lieu adjacent : lieuActuel.getLieuxAdjacents()) {
            vue.afficherMessage(" |--> " + adjacent.getNom());
        }
    }

    private void demanderLivre(String nomLivre) {
        Lieu courant = joueur.getPosition();

        if (!courant.getNom().equalsIgnoreCase("Salle_de_Lecture")) {
            vue.afficherMessage("Vous devez être dans la salle de lecture pour demander un livre.");
            return;
        }

        Optional<PNJ> pnjOpt = courant.getPNJs().stream()
                .filter(p -> p.getNom().equalsIgnoreCase("Bibliothecaire"))
                .findFirst();

        if (pnjOpt.isEmpty()) {
            vue.afficherMessage("Le bibliothécaire n'est pas là.");
            return;
        }

        PNJ bibliothecaire = pnjOpt.get();

        Optional<Objet> livreOpt = bibliothecaire.getSac().stream()
                .filter(o -> o.getNom().equalsIgnoreCase(nomLivre) && o instanceof Livre)
                .findFirst();

        if (livreOpt.isPresent()) {
            Objet livre = livreOpt.get();
            bibliothecaire.getSac().remove(livre);
            joueur.ramasser(livre);
            vue.afficherMessage("Le bibliothécaire vous donne : " + livre.getNom());
        } else {
            vue.afficherMessage("Le bibliothécaire n'a pas ce livre.");
        }
    }
}