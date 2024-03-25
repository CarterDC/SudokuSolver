:: Ce fichier batch envoie une série d'instruction à l'interpréteur de lignes de commande
:: 1 ) Compiler avec javac les fichiers sources .java en bytecode .class
:: 2) Packager ces .class en une archive java .jar avec un manifeste pour indiquer le point d'entrée
:: 3) Faire executer le .jar par la Java Virtual Machine

@echo off

REM Mise à jour de la variable d'environnement PATH (uniquement pour cette session de ligne de commande)
set path = "C:\Program Files\Microsoft\jdk-21.0.1.12-hotspot\bin"

REM vérification que tout va bien
echo.
echo Version de java
echo ---------------
java -version
echo.

echo Version du compilateur java
echo ---------------------------
javac -version
if ERRORLEVEL 1 (
    echo La commande javac -version ne s est pas executee comme il faut !
    echo Editez ce fichier pour mettre a jour la variable path.
    exit /b REM Stoppe l'execution du batch
)
echo.

echo Compilation des .class du projet Sudoku
echo ---------------------------------------

:: reproduit l'architecture de dossiers dans bin\
:: et compile aussi toutes les fichiers du dossier utils
javac -d bin src\Sudoku.java src\utils\*.java
:: vérif que la compilation s'est bien déroulée 
if ERRORLEVEL 1 (
    echo La commande javac ne s est pas executee comme il faut !
    exit /b REM Stoppe l'execution du batch
)
:: Affiche les fichiers du dossier bin (fichiers .class) 
dir /S /B bin\* 
echo.

echo Creation du .jar
echo -----------------
cd bin
jar cvmf ..\src\META-INF\MANIFEST.MF ..\monSudoku.jar Sudoku.class utils\*.class
cd ..
echo.

echo Execution du .jar
echo -----------------
echo.
java -jar monSudoku.jar -h
echo.