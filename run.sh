#!/usr/bin/env bash
set -euo pipefail

echo "Compilation en cours..."
javac -cp src/main/java $(find src/main/java -name "*.java")

echo "Compilation reussie. Lancement du jeu..."
java -cp src/main/java jeu.Main
