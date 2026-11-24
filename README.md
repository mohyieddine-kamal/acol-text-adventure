# ACOL — Text Adventure in a City

A console text adventure in Java: explore a small city, get past a locked
house, beat a guard at rock-paper-scissors to reach a treasure, and borrow
books from a librarian. Built as the implementation increment of an
object-oriented analysis & design project (UML use cases, class diagrams,
sequence diagrams — see `docs/rapport.pdf`).

## Architecture

- **`modele`**: the game world. `Lieu` (place) holds objects, NPCs, and a
  list of adjacent places — `ajouterAdjacent()` links two places both ways
  in one call. `Batiment` (building) extends `Lieu` with a lock/unlock
  mechanism (`Cle` → access code match) and its own interior place.
  `Personnage` (position, inventory, HP) is the base for both `Joueur` and
  `PNJ`. `Objet` is abstract (`utiliser(Personnage)`), specialized into
  `Cle`, `Tresor`, `Livre`, and `Arme` — the last one, along with `Rue`'s
  street length, is modeled for a later increment and isn't wired into
  this one's world yet.
- **`controleur.JeuControleur`**: builds the city graph once at startup,
  runs the main loop, and dispatches each typed command (`go`, `take`,
  `enter`, `talk`, `play`, `ask`, ...) to the matching world action.
- **`vue`**: `IHMTextuelle` reads/writes the console. `IHMGraphique`
  extends it with a Swing window instead (same `afficherMessage` /
  `lireCommande` contract, input handed over through a blocking queue so
  the Swing event thread and the game loop don't block each other) — a
  drop-in alternate view that isn't wired into `Main` yet, but works as-is.

## Build & run

```bash
./run.sh
```

or manually:

```bash
javac -cp src/main/java $(find src/main/java -name "*.java")
java -cp src/main/java jeu.Main
```

Type `menu` once the game starts for the full command list.

## Project structure

```
src/main/java/jeu/
  modele/      the game world (Lieu, Batiment, Personnage, Objet and subclasses, Ville)
  controleur/  JeuControleur (game loop, command dispatch), CommandeParser
  vue/         IHMTextuelle (console), IHMGraphique (Swing, unused by Main)
  Main.java    entry point
docs/rapport.pdf   UML analysis & design document (cahier des charges, use cases, class/sequence diagrams, user manual)
```

## Context

ACOL (Analyse et Conception Objet de Logiciels) course project at Grenoble
INP - Ensimag, 2025.
