package utils;

import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Classe de test des fonctions / méthodes publiques de la class Grid
 * <p>
 * Les 3 fonctions testées ici sont :
 * Optional<int[][][]> parseFileGrid(String fileName)
 * boolean hasDuplicates(int[][][] cellArray)
 * recurseSolve(SolveResult)
 */
public class GridTest {

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les noms de fichiers invalides
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal, mais un empty en cas d'erreur.
     * <p>
     * Le test s'assure que la fonction renvoie bien un empty quand lancée avec un nom de fichier inexistant
     */
    @Test
    public void testParseFileGrid_invalidFilename() {
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("fichier_inexistant.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les fichiers non-conformes
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal, mais un empty en cas d'erreur.
     * le fichier test_grille_01 ne comporte aucune ligne codante (que des commentaires et des espaces)
     * <p>
     * Le test s'assure que la fonction renvoie bien un empty quand lancée avec un fichier vide de toute grille
     */
    @Test
    public void testParseFileGrid_invalidGrid01() {
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("data\\test_grille_01.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les fichiers non-conformes
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal, mais un empty en cas d'erreur.
     * le fichier test_grille_02 ne comporte que des symboles valides, mais 3 lignes sont incomplètes
     * <p>
     * Le test s'assure que la fonction renvoie bien un empty quand la grille est incomplete
     */
    @Test
    public void testParseFileGrid_invalidGrid02() {
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("data\\test_grille_02.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les fichiers non-conformes
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal, mais un empty en cas d'erreur.
     * le fichier test_grille_03 comporte le bon nombre de lignes et de caractères
     * mais 2 caractères sont invalides : un chiffre (0) et un espace
     * <p>
     * Le test s'assure que la fonction renvoie bien un empty quand la grille comporte des caractères invalides
     */
    @Test
    public void testParseFileGrid_invalidGrid03() {
        // le fichier 
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("data\\test_grille_03.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les fichiers conformes
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal, mais un empty en cas d'erreur.
     * le fichier test_grille_04 comporte une grille valide dont certaines lignes ont des trailing spaces
     * <p>
     * Le test s'assure que la fonction parse cette grille et ne retourne pas un empty 
     */
    @Test
    public void testParseFileGrid_validGrid01() {
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("data\\test_grille_04.txt");

        Assert.assertNotEquals(testResult, Optional.empty());
    }

    /**
     * Ce test vérifie que la fonction parseFileGrid gère correctement les fichiers conformes
     * <p>
     * parseFileGrid renvoie un int[][][] en temps normal
     * le fichier test_grille_05 comporte une grille parsable dont les valeurs sont connues
     * <p>
     * Le test s'assure que la fonction parse cette grille en int[][][] dont chaque valeur est correcte
     */
    @Test
    public void testParseFileGrid() {
        Optional<int[][][]> testResult;
        testResult = Grid.parseFileGrid("data\\test_grille_05.txt");

        // création du tableau de référence qu'on doit obtenir
        // il doit être composé de 9 lignes de 9 cellules contenant chacune un tableau d'int (int[9][9][x])
        // les 8 premieres lignes sont composées de valeurs fixes de 1 à 9
        // la dernière ligne est composée de valeurs libres, donc 9 cellules contenant chacune un tableau des 9 candidats possibles
        int[][][] referenceCellArray = new int[9][9][];
        for (int lineIndex = 0; lineIndex < 8; lineIndex++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                referenceCellArray[lineIndex][columnIndex] = new int[]{columnIndex + 1};
            }
        }
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            referenceCellArray[8][columnIndex] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
        
        Assert.assertTrue(Arrays.deepEquals(referenceCellArray, testResult.get()));
    }

    /**
     * Ce test vérifie que la fonction hasDuplicates détecte correctement les doublons
     * <p>
     * hasDuplicate renvoie false si tout va bien, mais renvoie true si au moins un doublon a été détecté.
     * le fichier test_grille_06 comporte une grille parsable qui contient un doublon à la ligne 8
     * <p>
     * Le test s'assure que la fonction renvoie bien true lorsqu'il y a des doublons sur une même ligne
     */
    @Test
    void testHasDuplicates_lineDuplicates() {
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_06.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }

    /**
     * Ce test vérifie que la fonction hasDuplicates détecte correctement les doublons
     * <p>
     * hasDuplicate renvoie false si tout va bien, mais renvoie true si au moins un doublon a été détecté.
     * le fichier test_grille_07 comporte une grille parsable qui contient un doublon à la colonne 8
     * <p>
     * Le test s'assure que la fonction renvoie bien true lorsqu'il y a des doublons sur une même colonne
     */
    @Test
    void testHasDuplicates_columnDuplicates() {
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_07.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }
    
    /**
     * Ce test vérifie que la fonction hasDuplicates détecte correctement les doublons
     * <p>
     * hasDuplicate renvoie false si tout va bien, mais renvoie true si au moins un doublon a été détecté.
     * le fichier test_grille_08 comporte une grille parsable qui contient un doublon dans le carré 8
     * <p>
     * Le test s'assure que la fonction renvoie bien true lorsqu'il y a des doublons dans un même carré
     */
    @Test
    void testHasDuplicates_squareDuplicates() {
        // Note : L'indexage des carrés au sein de la grille suit la meme convention
        //  que l'indexage des cases dans un carré :
        //  0 1 2
        //  3 4 5
        //  6 7 8

        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_08.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }

    /**
     * Ce test vérifie que la fonction recurseSolve gère correctement les grilles impossibles
     * <p>
     * recurseSolve résout les grilles et renvoie entre 0 et nbMaxSolutions solutions
     * le fichier test_grille_09 comporte une grille parsable et sans doublon, mais qui ne peut pas être résolue
     * (cas où la seule valeur possible pour une cellule provoquerait un doublon)
     * <p>
     * Le test s'assure que la fonction ne trouve aucune solution à une grille impossible
     */
    @Test
    void testSolve_noSolution() {
        int nbMaxSolutions = 2;
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_09.txt");

        // on vérifie que la grille échappe bien à la vérification des doublons
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        // on vérifie qu'aucune solution n'a été trouvée
        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);
        
        Assert.assertTrue(testResult.getNbSolutions() == 0);
    }

    /**
     * Ce test vérifie que la fonction recurseSolve gère correctement les grilles a une solution
     * <p>
     * recurseSolve résout les grilles et renvoie entre 0 et nbMaxSolutions solutions
     * le fichier test_grille_10 comporte une grille classique de magazine, ne permettant qu'une solution unique
     * (test_grille_10_soluce contient sa solution sous forme parsable)
     * <p>
     * Le test s'assure que la fonction ne trouve bien qu'une seule solution et que cette solution est bien la bonne
     */
    @Test
    void testSolve_monoSolution() {
        // test de grille de magazine classique => une seule solution possible
        int nbMaxSolutions = 2;
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_10.txt");
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);
        
        // on vérifie qu'il n'y a bien qu'une seule solution (malgré qu'on en a demandé 2 au max)
        Assert.assertTrue(testResult.getNbSolutions() == 1);
        // on vérifie que c'est la bonne solution
        Optional<int[][][]> referenceCellArray = Grid.parseFileGrid("data\\test_grille_10_soluce.txt");

        Assert.assertTrue(Arrays.deepEquals(referenceCellArray.get(), testResult.getSolution(0)));
    }

    /**
     * Ce test vérifie que la fonction recurseSolve gère correctement les grilles a solutions multiples
     * <p>
     * recurseSolve résout les grilles et renvoie entre 0 et nbMaxSolutions solutions
     * le fichier test_grille_vide comporte une grille vide, avec donc 6x10^21 solutions possibles
     * <p>
     * Le test s'assure que la fonction ne trouve bien le nombre maximum de solutions demandées
     * Il vérifie aussi que chacune des solutions trouvées est une grille valide (qu'il n'y a pas eu d'erreur dans sa conception)
     * Il vérifie aussi que chacune des solutions trouvées est bien unique
     */
    @Test
    void testSolve_multipleSolutions() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // test de grille a multiples solutions possibles
        int nbMaxSolutions = 3000;
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseFileGrid("data\\test_grille_vide.txt");
        // Pas vraiment besoin de test de doublons sur une grille vide...
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);

        // on vérifie qu'il y a bien exactement le nombre de solutions demandées
        Assert.assertTrue(testResult.getNbSolutions() == nbMaxSolutions);

        // on vérifie que chaque solution est conforme aux règles
        // (aucun doublon de ligne de colonne ou de carré)
        int solutionIndex = 0;
        int[][][][] solutions = testResult.getSolutionsInstance();
        while (solutionIndex <= solutions.length - 1) {
            if(Grid.hasDuplicates(solutions[solutionIndex])){
                Assert.fail(MessageFormat.format("ERREUR : Doublon trouvé dans une solution a l index {0}", solutionIndex));
            }
            solutionIndex++;
        }
    
        // on vérifie que chaque solution est bien unique
        // pour ça on va calculer un hash
        // et tenter de l'ajouter dans un set (qui ne peut contenir que des valeurs uniques)
        // Note : la fonction deepHash utilisée à l'origine n'encode que sur 32 bits 
        // et provoque un grand nombre de collisions dès les premiers milliers de solutions
        // d'où l'utilisation d'un SHA256 à la place. 
        Set<String> hashes = new HashSet<>();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        solutionIndex = 0;

        while (solutionIndex <= solutions.length - 1) {
            byte[] encodedHash = digest.digest(Arrays.deepToString(solutions[solutionIndex]).getBytes("UTF-8"));
            String solutionHash = bytesToHex(encodedHash);
            
            if(!hashes.add(solutionHash)){
                //ajout impossible si le hash est déjà présent dans le set
                Assert.fail(MessageFormat.format("ERREUR : La solution a l index {0} existe deja", solutionIndex));
            }
            solutionIndex++;
        }
    }

    /**
     * Transforme un tableau de char en une chaîne hexadécimale
     * Fonction copié-collée depuis Copilot pour completer le hash en sha 
     * au lieu d'un simple int32 Arrays.deepHash
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}