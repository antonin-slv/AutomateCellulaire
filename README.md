# Implémentation d'automates cellulaires

Ce projet a pour but d'implémenter différents automates cellulaires en Java.
Il permet de visualiser l'évolution de ces automates dans une interface graphique.
Nous retrouvons notamment un automate 1D, un automate généralisant le jeu de la vie et un automate de simulant un feu de forêt.

> Les automates cellulaires sont des modèles mathématiques permettant de simuler l'évolution de cellules dans un espace donné. Chaque cellule évolue en fonction de son état initial et de l'état de ses voisins. Les automates cellulaires sont utilisés dans de nombreux domaines tels que l'informatique, la biologie, la physique ou encore la chimie.


## Installation et lancement

- **Clonez le dépôt :** Clonez ce dépôt sur votre machine locale en utilisant la commande git suivante :
```bash
git clone https://github.com/antonin-slv/AutomateCellulaire.git
```

- **Lancez le projet :** Pour lancer le projet, exécutez la commande suivante dans le dossier du projet :
```bash
./gradlew.bat run
```
ou bien celle-ci si vous êtes sur un système Linux :
```bash
./gradlew run 
```

- **Génération de la documentation :** Pour générer la documentation du projet, exécutez la commande suivante dans le dossier du projet :
```bash
./gradlew.bat javadoc
```


## Organisation des fichiers
Notre projet est divisé en plusieurs dossiers organisés de la manière suivante :
- **AutomateCellulaire/src/main/java :** classes Java du projet.
- **AutomateCellulaire/src/main/java/core :** classes de base pour les automates cellulaires.
- **AutomateCellulaire/src/main/java/gui :** classes Controller pour l'interface graphique.
- **AutomateCellulaire/src/main/java/rules :** classes appliquant les règles de transition des automates.
- **AutomateCellulaire/src/main/resources/javafx :** fichiers FXML pour l'affichage des fenêtres dans l'interface graphique.
- **AutomateCellulaire/rules :** fichiers de configuration des automates.
- **AutomateCellulaire/save :** fichiers de sauvegarde des grilles d'automates.
- **AutomateCellulaire/gradle :** fichiers de configuration pour Gradle.
- **AutomateCellulaire/build.gradle :** fichier pour la configuration du projet avec Gradle.
- **AutomateCellulaire/gradlew :** fichier pour l'exécution du projet.
- **AutomateCellulaire/graldew.bat :** fichier pour l'exécution du projet sur Windows.
- **AutomateCellulaire/README.md :** fichier README du projet.


## Automates implémentés

### Automate 1D

L'automate 1D est un automate cellulaire composé d'une seule dimension dans lequel chaque cellule peut prendre deux états différents : 0 ou 1. 
On donne un numéro à chaque règle de transition, celui-ci correspond à son écriture en binaire. Par exemple, la règle 30 est définie par le tableau suivant :

| 111 | 110 | 101 | 100 | 011 | 010 | 001 | 000 |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|  0  |  0  |  0  |  1  |  1  |  1  |  1  |  0  |

### Jeu de la vie

Le jeu de la vie est un automate cellulaire bidimensionnel dans lequel chaque cellule peut prendre deux états différents : morte ou vivante.
Les règles de transition sont les suivantes :
- Une cellule qui a moins de 2 voisins vivants ou plus de 3 voisins vivants meurt.
- Une cellule qui a exactement 3 voisins vivants devient vivante.
- Une cellule vivante qui a exactement 2 voisins vivants reste vivante.
- Une cellule morte qui a exactement 2 voisins vivants reste morte.

### Propagation d'un feu de forêt

L'automate de propagation d'un feu de forêt est composé de deux dimensions et chaque cellule peut prendre quatre états différents : vide, forêt, en feu, brûlé.
Les règles de transition sont les suivantes :
- Une cellule vide reste vide.
- Une cellule de forêt reste de forêt si elle n'a pas de voisin en feu.
- Une cellule de forêt prend feu si elle a un voisin en feu.
- Une cellule en feu devient brûlée.

Nous avons également ajouté un paramètre de probabilité pour la propagation du feu ainsi que pour l'apparition spontanée de feux.
Les voisins ont différents poids ce qui permet par exemple de simuler un vent qui propage le feu.

### Average - vagues de chaleur

Average est un automate cellulaire à dimension quelconque avec un nombre quelconque d'états.
Une cellule y prend la valeur moyenne de ses voisins.

### Règle de majorité

L'automate de la règle de majorité est de dimension quelconque et chaque cellule peut prendre des états différents.
La règle de transition est la suivante : une cellule prend la valeur majoritaire chez ses voisins.


## Mode d'emploi

Lorsque vous lancez le projet, vous arrivez sur le menu principal. 
Vous devez tout d'abord charger un automate existant en cliquant sur le bouton `Modify automate`.

Lorsque vous êtes dans le menu de création d'automate, vous pouvez :
- `LOAD`: sélectionner un automate existant dans la liste déroulante et le charger
- `SAVE`: sauvegarder l'automate actuel avec les modifications apportées en lui donnant un nom
- `isHexa`: cocher cette case si vous souhaitez que l'automate soit hexagonal
- Grille de voisinage : définir les poids des voisins 
- `Back`: retourner au menu principal

Vous pouvez définir un nouvel automate en créant un fichier de configuration json dans le dossier `AutomateCellulaire/rules`.
Dans ce fichier, vous devez définir les paramètres suivants :
- *dimension* : la dimension de l'automate
- *alphabet* : la liste des états possibles pour chaque cellule
- *colors* : la liste des couleurs associées à chaque état
- *voisinage* : la liste des coordonnées des voisins de chaque cellule, de forme [ y , x ]
- *regle* : un tableau contenant les règles de transition pour chaque état possible


>Note : Il faut respecter certaines consignes en fonction du type d'automate sélectionné :
>- *Hexagonal* :  Pour que l'affichage soit en accord avec les voisins directs, veillez à supprimer celui en haut à gauche et en bas à droite.
>- *Average*&*Majority* : Ce sont des règles "continues". Ne pas définir de couleur, mais le nombre de couleurs utilisées ["col1", "col2", ...] => ["nbCol"]. Il en va de même pour les états.
>- *somme* : Ne pas hésiter à prendre exemple sur le Jeu de la vie.<br/>
Il faut définir le poids de chaque voisin -> "weightNeighbour": [poidVoisin1, ...,poidVoisin N]<br/>
Même chose pour le poids des types -> "weightType": [poidType1, ...,poidTypeN]<br/>
Vous devez également écrire la probabilité qu'une sous-règle donne un état (la somme doit faire 1) -> "min:max": [proba1, ...,probaN]<br/>

Dans la fenêtre de modification de map `Modify map`, vous pouvez :
- `Load`: charger une grille existante
- `Save as`: sauvegarder la grille actuelle en lui donnant un nom
- Input `New :`: modifier la taille de la grille puis cliquer sur `apply` pour valider
- Input `Draw :`: choisir une couleur dans le menu déroulant puis choisir l'état correspondant et enfin cliquer sur une cellule pour la colorer
- Input `Replace :`: choisir le premier état qui sera remplacé par le second état puis cliquer sur `apply` pour valider
- Input `Random :`: choisir le nombre d'états différents puis cliquer sur `apply` pour générer une grille aléatoire
- `Back`: retourner au menu principal

Lors d'une simulation `Start`, vous avez la possibilité de réaliser différentes actions :
- `Play`: lance la simulation
- `Pause`: met en pause la simulation
- `>`: avance d'une étape dans la simulation
- Curseur `Speed`: permet de régler la vitesse de la simulation
- `Set cell's states`: permet de changer l'état d'une cellule en choisissant l'état dans le menu déroulant et en cliquant sur la cellule
- `Save`: sauvegarde la grille actuelle
- `Back to menu`: retourne au menu principal


## Auteurs
- **BLANCHÉ Thomas**
- **HONORÉ Alexandre**
- **SITHIDEJ Clara**
- **SYLVESTRE Antonin**