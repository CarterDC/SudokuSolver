   ____        __     __          ____     __               ___  ___  ___ ___
  / ____ _____/ ___  / /____ __  / _____  / _  _____ ____  / _ \/ _ \/ _ <  /
 _\ \/ // / _  / _ \/  '_/ // / _\ \/ _ \/ | |/ / -_/ __/  \_, / // / // / / 
/___/\_,_/\_,_/\___/_/\_\\_,_/ /___/\___/_/|___/\__/_/    /___/\___/\___/_/  
                                                                           
=============================================================================

Compilation & JARing :
----------------------
 Lancer le batch buildJar.bat devrait recompiler les sources vers le dossier bin\
 puis archiver les fichiers .class vers monSudoku.jar
 
 L archive Java est ensuite automatiquement lancée avec l option -h afin de dispenser l aide.
 Pour plus de confort, une ligne de commande est collée dans le presse papier ; 
  il suffit d appuyer sur ctrl + v puis enter pour lancer le programme
  avec des paramètres par default : fichier "grille1.txt" et "2" solutions au maximum.

Utilisation :
-------------

>> java -jar monSudoku.jar <nom_de_fichier> [<nombre_de_solutions>]
nom_de_fichier : nom complet avec chemin d acces et extension
nombre_de_solutions : paramètre optionnel, valeur par default : 2

>> java -jar monSudoku.jar -h 
pour l affichage de l aide

Contenu :
---------

Coirier__Romain\ : Racine du Projet
|
|-bin\ : Regroupe les fichiers .class dans une copie de l'arborescence de src\
|
|-data\ : Regroupe des fichiers-grille utilisés pour les tests,
|         des grilles du commerce et la grille d'Arto Inkala de 2012
|
|-doc\ : La documentation javaDoc du projet ;
|        le fichier Grid.java a été spécifiquement documenté avec le flag -private
|
|-src\
|  |-META-INF\ : Contient le manifeste qui indique la fonction main à utiliser comme entry point
|  |-utils\ : L'ensemble des Classes instanciables et des méthodes statiques utiles 
|  |-sudoku.java : Le fichier contenant le point d'entrée du programme 
|
|-test\utils\
|        |-GridTest.java : implémentation des tests jUnit des fonctions publiques de src\utils\Grid.java
|        |-resultats_tests.jpg : une copie ecran des results de tests de la classe GridTest
|
|-buildJar.bat : un script qui compile et archive le projet dans monSudoku.jar puis execute l'execute
|-grille1.txt : une copie de la grille fournie en exemple sur la plateforme du cnam
|-monSudoku.jar : Le projet compilé et archivé qui peut être lancé (cf Utilisation)
|-Readme.txt : Le présent fichier

Crédits :
---------

Romain COIRIER
email : romain.coirier.auditeur.lecnam.net
