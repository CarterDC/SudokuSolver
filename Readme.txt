   ____        __     __          ____     __               ___  ___  ___ ___
  / ____ _____/ ___  / /____ __  / _____  / _  _____ ____  / _ \/ _ \/ _ <  /
 _\ \/ // / _  / _ \/  '_/ // / _\ \/ _ \/ | |/ / -_/ __/  \_, / // / // / / 
/___/\_,_/\_,_/\___/_/\_\\_,_/ /___/\___/_/|___/\__/_/    /___/\___/\___/_/  
                                                                           
=============================================================================

Compilation & JARing :
----------------------
 Lancer le batch buildJar.bat devrait recompiler les sources vers le dossier bin\
 puis archiver les fichiers .class vers monSudoku.jar
 
 L archive Java est ensuite automatiquement lancee avec l option -h afin de dispenser l aide.
 Pour plus de confort, une ligne de commande est collee dans le presse papier ; 
  il suffit d appuyer sur ctrl + v puis enter pour lancer le programme
  avec des parametres par default : fichier "grille1.txt" et "2" solutions au maximum.

Utilisation :
-------------

>> java -jar monSudoku.jar <nom_de_fichier> [<nombre_de_solutions>]
nom_de_fichier : nom complet avec chemin d acces et extension
nombre_de_solutions : paramètre optionnel, valeur par default : 2

>> java -jar monSudoku.jar -h 
pour l affichage de l aide

Resources :
-----------
Un certain nombre de fichiers de sudoku sont disponibles dans le dossier data\ en plus du fichier grille1.txt present a la racine.
data\grille_1_1.txt

data, test, bin, doc etc...


Crédits :
---------

Romain COIRIER
email : romain.coirier.auditeur.lecnam.net
