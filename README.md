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

- **Installez Gradle :** Si vous ne possédez pas Gradle sur votre machine, vous pouvez l'installer en suivant les instructions disponibles [ici](https://gradle.org/install/).

- **Lancez le projet :** Pour lancer le projet, exécutez la classe `Main`.

## Automates implémentés

### Automate 1D

L'automate 1D est un automate cellulaire composé d'une seule dimension dans lequel chaque cellule peut prendre deux états différents : 0 ou 1. 
On donne un numéro à chaque règle de transition, celui-ci correspond à son écriture en binaire. Par exemple, la règle 30 est définie par le tableau suivant :

| 111 | 110 | 101 | 100 | 011 | 010 | 001 | 000 |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
|  0  |  0  |  0  |  1  |  1  |  1  |  1  |  0  |

### Règle de majorité

L'automate de la règle de majorité est de dimension quelconque et chaque cellule peut prendre deux états différents : 0 ou 1.
La règle de transition est la suivante : une cellule prend la valeur majoritaire de ses voisins.

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

## Définition d'un nouvel automate

Pour définir un nouvel automate, il existe différentes options :
- **Fichier de configuration :** Vous pouvez définir un nouvel automate en créant un fichier de configuration dans le dossier `AutomateCellulaire/src/main/resources/rules`.
    Dans ce fichier, vous devez définir les paramètres suivants :
    - *dimension* : la dimension de l'automate
    - *alphabet* : la liste des états possibles pour chaque cellule
    - *voisins* : la liste des coordonnées des voisins de chaque cellule
    - *regle* : un tableau contenant les règles de transition pour chaque état possible


## Auteurs
- **BLANCHÉ Thomas**
- **HONORÉ Alexandre**
- **SITHIDEJ Clara**
- **SYLVESTRE Antonin**