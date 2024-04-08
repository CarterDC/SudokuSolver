
package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.text.MessageFormat;
import java.util.regex.*;

/**
 * La classe Grid est une collection de méthodes statiques qui permettent la résolution de grilles de sudoku.
 * 
 * Le concept de "cellArray" est beaucoup utilisé et correspond à un tableau d'int à 3 dimensions.
 * soit 2 dimensions pour la "grille" de 9x9 
 * et la dernière dimension pour symboliser une "cellule" pouvant contenir plusieurs valeurs "candidats" potentielles. 
 * 
 * Note : Cette classe ne peut pas être instanciée.
 */
public class Grid {
    /**
     * Le nombre maximum de passes récursives autorisées, pour éviter le risque de boucle infinie.
     */
    public static final int MAX_PASS_RECURSIONS = 81; // arbitraire, certainement pas necessaire et sur-évalué 

    // Pour empêcher l'instanciation
    private Grid(){}

    /**********************************************
     **         Vérification des données         **
     **********************************************/

    /**
     * Vérifie si la chaîne fournie est conforme à la regex "^[1-9.]{9}$".
     * une ligne valide pour une grille de sudoku doit être composée de 9 caractères
     * les caractères autorisés sont les chiffres de 1 à 9 et le point "."
     * 
     * @param fileLine : une ligne extraite d'un fichier
     * @return boolean : true si la chaîne matche, false sinon
     */
    private static boolean isValidFileLine(String fileLine) {
        Pattern pattern = Pattern.compile("^[1-9.]{9}$");
        return pattern.matcher(fileLine).matches();
    }

    /**
     * // TODO : remettre commentaires a jour
     * Parse, si possible, un fichier "grille" en tableau d'int de 9x9.
     * Ne renvoie un int[][] uniquement que si le fichier a bien été trouvé
     * et qu'il contient exactement 9 lignes codants pour une grille de sudoku
     * renvoie un empty() sinon
     * 
     * @param fileName : le "chemin relatif + nom + extension" du fichier à parser,
     *                 ex : "data\grille_1_1.txt"
     * @return int[][] | Optional.empty()
     */
    public static Optional<int[][][]> parseGridFile(String fileName) {
        //
        int[][][] cellArray = new int[9][9][];

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            int lineIndex = 0;
            while (fileScanner.hasNextLine()) {
                // on lit une ligne sans les trailing spaces 
                String fileLine = fileScanner.nextLine().trim();
                if (isValidFileLine(fileLine)) {
                    // la ligne extraite contient exactement 9 caractères valides (1 à 9 + ".")
                    // on la parse caractère par caractère
                    int columnIndex = 0;
                    for (char myChar : fileLine.toCharArray()) {
                        // Note : getNumericValue(".") renvoie -1
                        int numericChar = Character.getNumericValue(myChar);
                        if (numericChar == -1) {
                            cellArray[lineIndex][columnIndex] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
                        } else {
                            cellArray[lineIndex][columnIndex] = new int[] { numericChar };
                        }
                        columnIndex++;
                    }
                    lineIndex++;
                }
            }
            fileScanner.close();

            // une grille de sudoku n'est valide que si elle contient exactement 9 lignes
            // valides
            if (lineIndex != 9) {
                throw new CustomException("" + lineIndex);
            }

        } catch (FileNotFoundException e) {
            String errMsg = MessageFormat.format("ERREUR : Impossible de trouver le fichier {0} !", fileName);
            System.err.println(errMsg);
            return Optional.empty();
        } catch (CustomException e) {
            String errMsg = MessageFormat.format(
                    "ERREUR : Le fichier {0} ne comporte pas le bon nombre de lignes valides ({1}/9) !",
                    fileName, e.getMessage());
            System.err.println(errMsg);
            return Optional.empty();
        }

        return Optional.of(cellArray);
    }

    /**
     * Renvoie le premier doublon trouvé dans un tableau de chiffres
     * Si aucun doublon n'est trouvé, renvoie empty()
     * 
     * @param digitArray : un tableau de chiffres [0 à 9]
     * @return Optional<Integer> : le doublon trouvé ou bien empty()
     */
    private static Optional<Integer> getDuplicates(int[] digitArray) {
        //
        boolean[] digitsSeen = new boolean[10]; // pour chaque chiffre, de 0 à 9

        int index = 0;
        while (index <= digitArray.length - 1) {
            if (digitsSeen[digitArray[index]] == true) {
                // on renvoie le premier doublon trouvé
                return Optional.of(digitArray[index]);
            } else {
                digitsSeen[digitArray[index]] = true;
                index++;
            }
        }
        // on n'a trouvé aucun doublon
        return Optional.empty();
    }

    /**
     * Vérifie chacune des 9 lignes, 9 colonnes et chacun des 9 carrés à la
     * recherche de doublons
     * Une grille qui contient un doublon est invalide et ne peut pas être résolue
     * En cas de doublon, affiche un message avec la valeur incriminée et sa
     * position
     * 
     * @param cellArray : la grille a vérifier
     * @return boolean : true si un doublon a été trouvé dans la grille fournie
     */
    public static boolean hasDuplicates(int[][][] cellArray) {
        boolean hasDuplicates = false;
        Optional<Integer> duplicatedDigit;
        try {
            for (FilterType filterType : FilterType.values()) {
                for (int index = 0; index < 9; index++) {
                    // les filtres de ligne, colonne et carré ne renvoient que les valeurs
                    // définitives et pas les candidats multiples
                    int[] myFilter = getFilter(cellArray, index, filterType);
                    duplicatedDigit = getDuplicates(myFilter);
                    if (duplicatedDigit.isPresent()) {
                        throw new CustomException(
                                new String[] { getFilterName(filterType), "" + index, "" + duplicatedDigit.get() });
                    }
                }
            }
        } catch (CustomException e) {
            // on indique bien qu'il ya des doublons
            hasDuplicates = true;
            String errMsg = MessageFormat.format("ERREUR : {0} numero {1}, doublon de {2} !", e.myArgs[0], e.myArgs[1],
                    e.myArgs[2]);
            System.err.println(errMsg);
        }
        return hasDuplicates;
    }

    /**********************************************
     **        Lignes, Colonnes et Carrés        **
     **********************************************/

    /**
     * Enum des 3 façons dont on peut filter une grille
     * par Ligne, Colonne ou Carré
     */
    private static enum FilterType {
        LINE, // 0
        COLUMN, // 1
        SQUARE // 2
    }

    /**
     * Calcule et renvoie l'index du carré qui contient la cellule définie par
     * les coordonnées (lineIndex, columnIndex).
     * Note : L'indexage des carrés au sein de la grille suit la meme convention
     * que l'indexage des cases dans un carré :
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * 
     * @param lineIndex   : un numéro de ligne entre 0 et 8
     * @param columnIndex : un numéro de colonne entre 0 et 8
     * @return int : un index de carré entre 0 et 8
     */
    private static int getSquareIndex(int lineIndex, int columnIndex) {
        return (lineIndex / 3) * 3 + columnIndex / 3;
    }

    /**
     * Retourne la liste des chiffres définitivement placés d'une ligne en
     * particulier
     * Les cellules contenant des candidats multiples ne sont pas prises en compte
     * 
     * @param cellArray
     * @param lineIndex
     * @return int[]
     */
    private static int[] getLineFilter(int[][][] cellArray, int lineIndex) {
        // La taille maxi est 9 pour une ligne entièrement résolue
        int[] myLine = new int[9];
        int nbValues = 0;
        // on parcours toutes les colonnes d'une ligne donnée
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            int[] currentCell = cellArray[lineIndex][columnIndex];
            if (currentCell.length == 1) {
                // il n'y a bien qu'un seul candidat dans cette cellule
                myLine[nbValues] = currentCell[0];
                nbValues++;
            }
        }
        // on renvoie un tableau de la bonne taille
        return Arrays.copyOf(myLine, nbValues);
    }

    /**
     * Retourne la liste des chiffres définitivement placés d'une colonne en
     * particulier
     * Les cellules contenant des candidats multiples ne sont pas prises en compte
     * 
     * @param cellArray
     * @param columnIndex
     * @return int[]
     */
    private static int[] getColumnFilter(int[][][] cellArray, int columnIndex) {
        //
        int[] myColumn = new int[9];
        int nbValues = 0;
        // On parcours toutes les lignes d'une colonne donnée
        for (int lineIndex = 0; lineIndex < 9; lineIndex++) {
            int[] currentCell = cellArray[lineIndex][columnIndex];
            if (currentCell.length == 1) {
                // il n'y a bien qu'un seul candidat dans cette cellule
                myColumn[nbValues] = currentCell[0];
                nbValues++;
            }
        }
        return Arrays.copyOf(myColumn, nbValues);
    }

    /**
     * Retourne la liste des chiffres définitivement placés d'un carré en
     * particulier
     * Les cellules contenant des candidats multiples ne sont pas prises en compte
     * 
     * @param cellArray
     * @param squareIndex
     * @return int[]
     */
    private static int[] getSquareFilter(int[][][] cellArray, int squareIndex) {
        // L'indexage des cellules dans le carré suit la meme convention
        // que l'indexage des carrés au sein de la grille
        // 0 1 2
        // 3 4 5
        // 6 7 8

        int[] mySquare = new int[9];
        int nbValues = 0;
        // transformation de l'index du carré en décalage de coordonnées
        int lineOffset = (int) squareIndex / 3;
        int columnOffset = squareIndex % 3;

        for (int lineIndex = 0; lineIndex < 3; lineIndex++) {
            for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
                int[] currentCell = cellArray[lineOffset * 3 + lineIndex][columnOffset * 3 + columnIndex];
                if (currentCell.length == 1) {
                    // il n'y a bien qu'un seul candidat dans cette cellule
                    mySquare[nbValues] = currentCell[0];
                    nbValues++;
                }
            }
        }
        return Arrays.copyOf(mySquare, nbValues);
    }

    /**
     * Retourne la liste des chiffres définitif de la ligne, de la colonne ou du
     * carré d'index donné
     * Ex LINE 0, COLUMN 3 ou SQUARE 8
     * 
     * @param cellArray
     * @param filterIndex : un index compris entre 0 et 8 inclus
     * @param filterType  : une valeur d'Enum valide
     * @return int[]
     */
    private static int[] getFilter(int[][][] cellArray, int filterIndex, FilterType filterType) {
        int[] myFilter;

        switch (filterType) {
            case LINE:
                myFilter = getLineFilter(cellArray, filterIndex);
                break;
            case COLUMN:
                myFilter = getColumnFilter(cellArray, filterIndex);
                break;
            default: // case SQUARE:
                myFilter = getSquareFilter(cellArray, filterIndex);
                break;
        }
        return myFilter;
    }

    /**
     * Retourne la liste des chiffres définitif de la ligne ou de la colonne d'un
     * index donné
     * Ou bien du carré contenant la cellule définie par les coordonnées (lineIndex,
     * columnIndex)
     * 
     * @param cellArray
     * @param lineIndex
     * @param columnIndex
     * @param filterType
     * @return int[]
     */
    private static int[] getFilter(int[][][] cellArray, int lineIndex, int columnIndex, FilterType filterType) {
        int[] myFilter;

        switch (filterType) {
            case LINE:
                myFilter = getLineFilter(cellArray, lineIndex);
                break;
            case COLUMN:
                myFilter = getColumnFilter(cellArray, columnIndex);
                break;
            default: // case SQUARE:
                myFilter = getSquareFilter(cellArray, getSquareIndex(lineIndex, columnIndex));
                break;
        }
        return myFilter;
    }

    /**
     * Retourne le nom du type de filtre associé à chaque valeur de l'Enum
     * utilisé uniquement pour le message d'erreur de doublons
     * 
     * @param filterType
     * @return String
     */
    private static String getFilterName(FilterType filterType) {
        String filterName;
        switch (filterType) {
            case LINE:
                filterName = "Ligne";
                break;
            case COLUMN:
                filterName = "Colonne";
                break;
            default: // case SQUARE:
                filterName = "Carre";
                break;
        }
        return filterName;
    }

    /**
     * Retourne une copie profonde d'un tableau de 9x9 cellules
     * 
     * @param origCellArray
     * @return int[][][] : un tableau ne contenant aucune des instances d'origine
     */
    public static int[][][] cellArrayDeepCopy(int[][][] origCellArray) {
        int[][][] newCellArray = new int[9][9][];
        for (int lineIndex = 0; lineIndex < 9; lineIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                newCellArray[lineIndex][columnIndex] = Arrays.copyOf(
                        origCellArray[lineIndex][columnIndex], origCellArray[lineIndex][columnIndex].length);
            }
        }
        return newCellArray;
    }

    /**********************************************
     **              Résolution                  **
     **********************************************/

    /**
     * Vérifie si un int est présent dans le tableau fourni
     * 
     * @param intToFind 
     * @param arrayToCheck
     * @return boolean : true si l'int recherché a été trouvé, false sinon
     */
    private static boolean isIn(int intToFind, int[] arrayToCheck) {
        boolean found = false;
        int index = 0;
        while (!found && index <= (arrayToCheck.length - 1)) {
            found = arrayToCheck[index] == intToFind;
            index++;
        }
        return found;
    }
    
    /** 
     * Filtre le tableau de base pour ne laisser que les valeurs non présentes dans le filtre.
     * @param baseArray
     * @param filterArray
     * @return int[] les valeurs du tableau de base moins celles présentes dans le filtre
     */
    private static int[] getDifference(int[] baseArray, int[] filterArray) {
        int[] newArray = new int[baseArray.length];
        int newLength = 0;
        for (int index = 0; index <= baseArray.length - 1; index++) {
            if (!isIn(baseArray[index], filterArray)) {
                // la valeur n'est pas dans le filtre, on l'ajoute à notre nouvelle liste
                newArray[newLength] = baseArray[index];
                newLength++;
            }
        }
        // renvoie un tableau qui fait exactement la bonne taille
        return Arrays.copyOf(newArray, newLength);
    }
    
    /** 
     * TODO : refaire les comments
     * Effectue une passe sur un tableau de cellules et renvoie une instance de "Résultat de la passe"
     * Une passe passe en revue successivement chaque ligne, colonne et carré
     * pour réduire le nombre de candidats possibles et placer des chiffres de façon définitive
     * 
     * @param currentCellArray
     * @return PassResult
     */
    private static PassResult recursePass(PassResult currentPassResult, int recursionCounter) {
        //
        PassResult newPassResult = new PassResult(currentPassResult.getCellArray());
        newPassResult.setNbPasses(recursionCounter + 1);
        int[][][] newCellArray = newPassResult.getCellArray();

        for (int lineIndex = 0; lineIndex < 9; lineIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                int nbCurrentCandidates = newCellArray[lineIndex][columnIndex].length;
                if ( nbCurrentCandidates > 1 ) {
                    // on ne vérifie que les cellules contenant de multiples candidats (length > 1)
                    int[] newCandidates = getNewCandidates(newCellArray, lineIndex, columnIndex);
                    if ( newCandidates.length == 0 ) {
                        newPassResult.setUnsolvable();                        
                        return newPassResult;
                    }
                    if (newCandidates.length > 1) {
                        // il reste encore de multiples candidats pour cette cellule
                        newPassResult.setHasMultipleCandidates();
                    }
                    if (newCandidates.length != nbCurrentCandidates ) {
                        // on indique qu'il ya eu (au moins) une modification du tableau durant cette passe
                        newPassResult.setDirty();
                        // on mets à jour la liste des candidats
                        newCellArray[lineIndex][columnIndex] = newCandidates;                        
                    }
                    newCellArray[lineIndex][columnIndex] = newCandidates;
                }
            }
        }

        if (newPassResult.needsRecursion() && recursionCounter < Grid.MAX_PASS_RECURSIONS) {
            recursionCounter++;
            return recursePass(newPassResult, recursionCounter);
        }
        return newPassResult;
    }
    
    /** 
     * Renvoie toutes les valeurs possible pour la cellule aux coordonnées (lineIndex, columnIndex)
     * <p>
     * Trouve les nouveaux candidats en faisant la différence entre la liste actuelle et chaque filtre successif
     * filtre de ligne, de colonne et de carré
     * @param cellArray
     * @param lineIndex 
     * @param columnIndex
     * @return int[] 
     */
    private static int[] getNewCandidates(int[][][] cellArray, int lineIndex, int columnIndex) {
        int[] currentCandidates = cellArray[lineIndex][columnIndex];
        int[] newCandidates = Arrays.copyOf(currentCandidates, currentCandidates.length);

        int filterIndex = 0;
        while (filterIndex < 3 && newCandidates.length > 0) {
            int[] myFilter = getFilter(cellArray, lineIndex, columnIndex, FilterType.values()[filterIndex]);
            newCandidates = getDifference(newCandidates, myFilter);
            filterIndex++;
        }

        return newCandidates;
    }

    public static void recurseSolve(SolveResult solveResult) {
        solveResult.incRecursionCounter();

        long startingTime = System.nanoTime();
        solveResult.passResult = recursePass(solveResult.passResult, 0);
        long endingTime = System.nanoTime();

        //faire le process du passResult pour déterminer la prochaine action, return ou recurse
        solveResult.processPassResult();

        if (solveResult.needsRecursion()) {
            // 
            int[][][][] cellArrayFork = createFork(solveResult.passResult.getCellArray());
            solveResult.passResult.setCellArray(cellArrayFork[0]);
            Grid.recurseSolve(solveResult);
            // vérif si on a notre nbre de soluces
            if (!solveResult.isFull()) {
                // on n'a pas atteint notre quota de soluces, on continue avec le second embranchement
                solveResult.passResult.setCellArray(cellArrayFork[1]);
                Grid.recurseSolve(solveResult);
            }
        }
        return;
    }

    /*
    public static SolveResult solve(int[][][] cellArray, int nbMaxSolutions) {
        //TODO : Refactor this shit !
        long startingTime = System.nanoTime();
        
        PassResult passResult = recursePass(new PassResult(cellArray), 0);

        long endingTime = System.nanoTime();        
        SolveResult result = new SolveResult(nbMaxSolutions, passResult, endingTime - startingTime);

        if (result.needsRecursion()) {
            // trouver la premiere case avec multiples candidats
            // faire une soluce avec la première valeur.
            // faire une soluce avec toutes les autres.
            int[][][] currentCellArray = passResult.getCellArray();
            int[] coords = getFirstForkPoint(currentCellArray);
            int[] currentCandidates = currentCellArray[coords[0]][coords[1]];

            int[][][] newCellArray = cellArrayDeepCopy(currentCellArray);
            newCellArray[coords[0]][coords[1]] = new int[] { currentCandidates[0] };
            // lancer une solve avec ce cellArray
            result.aggregate(solve(newCellArray, nbMaxSolutions));
            // vérif si on a notre nbre de soluces
            if (!result.isFull()) {
                //on n'a pas atteint notre quota de soluces, on continue avec le reste
                // second cellArray
                newCellArray = cellArrayDeepCopy(currentCellArray);
                int[] remainder = Arrays.copyOfRange(currentCandidates, 1, currentCandidates.length);
                newCellArray[coords[0]][coords[1]] = remainder;
                result.aggregate(solve(newCellArray, nbMaxSolutions));
            }
        }
        return result;
    }*/

    private static int[][][][] createFork(int[][][] cellArrayToSplit) {
        // création du tableau d'embranchement
        int[][][][] cellArrayFork = new int[2][][][];
        cellArrayFork[0] = Grid.cellArrayDeepCopy(cellArrayToSplit);
        cellArrayFork[1] = Grid.cellArrayDeepCopy(cellArrayToSplit);

        // trouver la premiere case avec multiples candidats
        int[] coords = getFirstForkPoint(cellArrayToSplit);
        int[] forkCandidates = cellArrayToSplit[coords[0]][coords[1]];

        // on crée l'embranchement : 
        // le premier tableau contient la première valeur de forkCandidates
        // le second tableau, toutes les autres valeurs
        cellArrayFork[0][coords[0]][coords[1]] = new int[]{forkCandidates[0]};
        int[] remainder = Arrays.copyOfRange(forkCandidates, 1, forkCandidates.length);
        cellArrayFork[1][coords[0]][coords[1]] = remainder;

        return cellArrayFork;
    }

    /** 
     * Renvoie les coordonnées (ligne, colonne) de la première cellule trouvée qui contient plusieurs candidats
     * Cette fonction n'est utilisée que sur des tableaux "multi-candidats" d'où l'absence de vérifications qu'on a bien trouvé
     * @param cellArray 
     * @return int[] les coordonnées de la cellule sous la forme d'un tableau {ligne, colonne}
     */
    private static int[] getFirstForkPoint(int[][][] cellArray) {
        int lineIndex = 0;
        int columnIndex = 0;
        boolean found = false;
        
        // sort des deux boucles dès qu'on a trouvé la première cellule "multi-candidats"
        do {
            columnIndex = 0;
            do{
                found = cellArray[lineIndex][columnIndex].length > 1;
            }
            while (!found && ++columnIndex <= cellArray[lineIndex].length - 1);                
        } while (!found && ++lineIndex <= cellArray.length - 1);

        return new int[] { lineIndex, columnIndex };
    }
    
    /** 
     * Renvoie une string contenant une grille prete à afficher
     * <p>
     * Les cellules indéterminées (contenant plusieurs candidats potentiels) sont affichées avec un "." 
     * @param cellArray la grille à afficher
     * @return String
     */
    public static String cellArrayToString(int[][][] cellArray) {
        String returnString = "";

        returnString += " -------------------------";
        for (int lineIndex = 0; lineIndex < 9; lineIndex++) {
            // création d'une ligne caractere par caractere
            String lineString = "\n |";
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                String myChar = (cellArray[lineIndex][columnIndex].length == 1)
                        ? String.valueOf(cellArray[lineIndex][columnIndex][0])
                        : ".";
                lineString += " " + myChar;
                // ajoute un pipe toutes les 3 colonnes
                if (columnIndex % 3 == 2) {
                    lineString += " |";
                }
            }
            returnString += lineString;
            // ajoute une ligne de séparation toutes les 3 lignes
            if (lineIndex % 3 == 2) {
                returnString +="\n -------------------------";
            }
        }
        return returnString + "\n";
    }
}