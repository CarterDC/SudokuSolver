   _____           __      __            _____       __                   _____ ____  ____  ____ 
  / ___/__  ______/ ____  / /____  __   / ___/____  / _   _____  _____   |__  // __ \/ __ \/ __ \
  \__ \/ / / / __  / __ \/ //_/ / / /   \__ \/ __ \/ | | / / _ \/ ___/    /_ </ / / / / / / / / /
 ___/ / /_/ / /_/ / /_/ / ,< / /_/ /   ___/ / /_/ / /| |/ /  __/ /      ___/ / /_/ / /_/ / /_/ / 
/____/\__,_/\__,_/\____/_/|_|\__,_/   /____/\____/_/ |___/\___/_/      /____/\____/\____/\____/  
                                                                                                 
==================================================================================================

Compilation & JARing :
---------------------- 
 Lancer le batch buildJar.bat devrait recompiler les sources vers le dossier bin\
 puis archiver les fichiers .class vers monSudoku.jar
 
 L'archive Java est ensuite automatiquement lancée avec l'option -h afin de dispenser l'aide.

Utilisation :
-------------

java -jar monSudoku.jar <nom_de_fichier> [<nombre_de_solutions>]

Un certain nombre de fichiers de sudoku sont disponibles dans le dossier data\ en plus du fichier grille1.txt présent à la racine.
Le nombre de solutions par défault est 2. Ce paramètre est optionnel.


Resources :
-----------

data\grille_1_1.txt


Crédits :
---------

Romain COIRIER
email : romain.coirier.auditeur.lecnam.net
