package utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.junit.Assert;

/**
 * Classe de test des fonctions / méthodes publiques de la class Grid
 * <p>
 * Les 3 fonctions testées ici sont :
 * Optional<int[][][]> parseGridFile(String fileName)
 * boolean hasDuplicates(int[][][] cellArray)
 * solve
 */
public class GridTest {

    @Test
    public void testParseGridFile_invalidFilename() {
        // si le nom de fichier n'est pas correct, la fonction doit renvoyer un empty
        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("fichier_inexistant.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    @Test
    public void testParseGridFile_invalidGrid01() {
        // le fichier ne comporte aucune ligne codante
        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("data\\test_grille_01.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    @Test
    public void testParseGridFile_invalidGrid02() {
        // le fichier ne comporte que des symboles valides, mais 3 lignes sont incomplètes
        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("data\\test_grille_02.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    @Test
    public void testParseGridFile_invalidGrid03() {
        // le fichier comporte le bon nombre de lignes et de caractères
        // mais 2 caractères sont invalides : un chiffre (0) et un espace
        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("data\\test_grille_03.txt");

        Assert.assertEquals(testResult, Optional.empty());
    }

    @Test
    public void testParseGridFile_validGrid01() {
        // le fichier comporte une grille valide
        // mais certaines lignes ont des trailing spaces
        // la fonction ne doit pas renvoyer un empty mais bien un int[][][]
        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("data\\test_grille_04.txt");

        Assert.assertNotEquals(testResult, Optional.empty());
    }

    @Test
    public void testParseGridFile() {
        // Teste si le parsing d'une grille valide particulière
        // renvoie bien le bon tableau à 3 dimensions avec les bonnes valeurs
        // Note : assertArrayEqual n'est pas adapté pour des tableaux de 2+ dimensions

        Optional<int[][][]> testResult;
        testResult = Grid.parseGridFile("data\\test_grille_05.txt");

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

    @Test
    void testHasDuplicates_lineDuplicates() {
        // Teste la capacité à détecter des doublons dans une grille correctement parsée
        // la grille contient un doublon sur la ligne d'index 8
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseGridFile("data\\test_grille_06.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }

    @Test
    void testHasDuplicates_columnDuplicates() {
        // Teste la capacité à détecter des doublons dans une grille correctement parsée
        // la grille contient un doublon sur la colonne d'index 
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseGridFile("data\\test_grille_07.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }

    @Test
    void testHasDuplicates_squareDuplicates() {
        // Teste la capacité à détecter des doublons dans une grille correctement parsée
        // la grille contient un doublon dans le carré d'index 
        // Note : L'indexage des carrés au sein de la grille suit la meme convention
        //  que l'indexage des cases dans un carré :
        //  0 1 2
        //  3 4 5
        //  6 7 8

        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseGridFile("data\\test_grille_08.txt");

        Assert.assertTrue(Grid.hasDuplicates(testCellArray.get()));
    }

    @Test
    void testSolve_noSolution() {
        // test d'une grille impossible
        // grille valide, sans doublons, mais avec une impossibilité selon les règles
        // cas où la seule valeur possible provoquerait un doublon
        int nbMaxSolutions = 2;
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseGridFile("data\\test_grille_09.txt");

        // on vérifie que la grille échappe bien à la vérification des doublons
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        // on vérifie qu'aucune solution n'a été trouvée
        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);
        
        Assert.assertTrue(testResult.getNbSolutions() == 0);
    }

    @Test
    void testSolve_monoSolution() {
        // test de grille de magazine classique => une seule solution possible
        int nbMaxSolutions = 2;
        Optional<int[][][]> testCellArray;
        testCellArray = Grid.parseGridFile("data\\test_grille_10.txt");
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);
        
        // on vérifie qu'il n'y a bien qu'une seule solution (malgré qu'on en a demandé 2)
        Assert.assertTrue(testResult.getNbSolutions() == 1);
        // on vérifie que c'est la bonne solution
        Optional<int[][][]> referenceCellArray = Grid.parseGridFile("data\\test_grille_10_soluce.txt");

        Assert.assertTrue(Arrays.deepEquals(referenceCellArray.get(), testResult.getSolution(0)));
    }

    @Test
    void testSolve_multipleSolutions() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // test de grille a multiples solutions possibles
        int nbMaxSolutions = 3000;
        Optional<int[][][]> testCellArray;
        // testCellArray = Grid.parseGridFile("data\\test_grille_vide.txt");
        testCellArray = Grid.parseGridFile("grille1.txt");
        // Pas vraiment besoin de test de doublons sur une grille vide...
        Assert.assertFalse(Grid.hasDuplicates(testCellArray.get()));

        SolveResult testResult = new SolveResult(testCellArray.get(), nbMaxSolutions);
        Grid.recurseSolve(testResult);

        // on vérifie s'il y a bien, au minimum 1 solution
        // et au maximum, le nombre de solutions demandées
        Assert.assertTrue(testResult.getNbSolutions() >= 1);
        Assert.assertTrue(testResult.getNbSolutions() <= nbMaxSolutions);

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
        // pour ça on va calculer un "deephash" (sinon ça ne prend pas en compte le contenu du tableau)
        // et tenter de l'ajouter dans un set (qui ne peut contenir que des valeurs uniques)
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