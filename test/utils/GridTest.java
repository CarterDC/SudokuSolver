package utils;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

/**
 * Classe de test des fonctions / méthodes publiques de la class Grid
 */
public class GridTest {
    @Test
    void testParseGridFile() {
        Optional<int[][][]> testResult;
        // test de fichiers non valides
        testResult = Grid.parseGridFile("fichier_inexistant.txt");
        Assert.assertEquals(testResult, Optional.empty());


        // test qu'un fichier donné est bien converti dans le tableau qui convient 
    }

    @Test
    void testHasDuplicates() {
        int[][][] cellArray = new int[9][9][];
        boolean myResult = Grid.hasDuplicates(cellArray);
    }

    @Test
    void testSolve() {
        //test de solution simple
        // test de grille impossible
        // test de grille de magazine
        
        // test de solutions multiples
        // vérification que chaque grille est bien valide (aucun doublons)
        // vérification que chaque grille st unique
    }
}
