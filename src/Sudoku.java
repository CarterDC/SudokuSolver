// TODO : faire une grille sans doublon mais qui ne soit pas faisable
import utils.*;

import java.util.Optional;
/**
 * Classe principale (Main) du programme de résolution de grilles Sudoku
 * <p>
 * Projet développé en java, dans le cadre de l'UE NFP136
 * usage : {@code java Sudoku <fileName> [<nbSolutions>]}
 * @author Romain COIRIER
 */
public class Sudoku {
   
    /**
     * Point d'entrée du programme
     * <p>
     * Gère le déroulement du programme : récupération des arguments,
     * vérification des données, solutionnement de la grille proposée et enfin affichage des résultats.
     * @param args
     */
    public static void main(String[] args) {

        Arguments myArgs = Arguments.parseArguments(args);
        if(myArgs.shouldDisplayHelp()) {displayHelp(); return;}

        // Transforme si possible le fichier fourni en tableau d'int @see #utils.Grid
        Optional<int[][][]> cellArray = Grid.parseGridFile(myArgs.getFileName());
        if (cellArray.isEmpty()) { return; }
        // a ce point on sait qu'on a bien 9 lignes de 9 cases, composées exclusivement des 9 chiffres
        // reste a vérifier l'absence de doublons sur chaque ligne, chaque colonne et chaque carré.
        System.out.println("Le fichier fourni contient bien une grille de 9x9 valeurs valides.");  

        if (Grid.hasDuplicates(cellArray.get())) { return; }
        // à ce point on sait que la grille est valide
        // reste a vérifier si elle a bien des solutions
        System.out.println("La grille fournie ne contient aucun doublons.");

        //affichage de la grille d'origine pour référence
        Grid.displayGrid(cellArray.get());
        
        // Résolution :
        // Effectue plusieurs passes successives pour tenter de trouver le nombre de solutions passé en paramètre
        SolveResult result = Grid.solve(cellArray.get(), myArgs.getNbMaxSolutions());
        // Affichage des résultats
        result.displayGrids();
        result.displayStats();
    }

    private static void displayHelp(){
        String helpString;
        helpString = "Usage : java -jar monSudoku.jar <file_path> [<nb_solutions>]\n" +
                     "        java -jar monSudoku.jar -h | --help\n" +
                     "\n" + 
                     "<file_path> : Le chemin relatif vers un fichier grille au format .txt.\n" +
                     "<nb_solutions> : Le nombre maximal de solutions a retourner (optionel, 2 par defaut).\n" +
                     "\n" +
                     "-h, --help : Affiche le present message d aide.";

        System.out.println(helpString);
    }

    private Sudoku(){}

}


