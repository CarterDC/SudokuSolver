package utils;

import java.text.MessageFormat;

/**
 * Classe qui compile les solutions trouvées ainsi que les statistiques d'execution
 * <p>
 * 
 */
public class SolveResult {
    
    private int[][][][] solutions;
    public PassResult passResult;
        
    private int nbMaxSolutions = 0;
    // stats
    private int nbRecursions = -1;
    private int nbPasses = 0;
    private int nbUnsolvableGrids = 0;
    private int nbFailedGrids = 0;
    private long nbNanoSeconds = 0;


    public SolveResult(int[][][] currentCellArray, int nbMaxSolutions){
        this.passResult = new PassResult(currentCellArray);
        this.nbMaxSolutions = nbMaxSolutions;
    }

    public void processPassResult() {
        // TODO : deal with time counter at some point
        this.nbPasses += this.passResult.getNbPasses();

        if(this.passResult.isUnsolvable()) {
            this.nbUnsolvableGrids++;
            return;
        }
        if(this.passResult.getNbPasses() >= Grid.MAX_PASS_RECURSIONS){
            this.nbFailedGrids++;
            return;
        }
        if(this.passResult.isSolved()){
            this.addSolution(this.passResult.getCellArray());
            return;
        }

    }

    public boolean needsRecursion(){
        
        if (this.passResult.isUnsolvable()) { return false;}
        if (this.passResult.getNbPasses() >= Grid.MAX_PASS_RECURSIONS) { return false;}
        if (this.passResult.isSolved()) { return false;}
        if (this.isFull()) { return false;}
        return this.passResult.hasMultipleCandidates() && !this.passResult.isDirty();
    }

    public void aggregate(SolveResult otherResult) {

        this.nbMaxSolutions = otherResult.nbMaxSolutions; // TODO : virer ça à terme
        //this.stats.aggregate(otherResult.getStats());
        // ! On ne copie pas le flag "needsRecursion"

        // on aggrége toutes les solutions
        int nbSolutionToAdd = otherResult.getNbSolutions();
        if( nbSolutionToAdd == 0) {
            // pas de soluces à ajouter
            return;
        }
        if( this.getNbSolutions() == 0) {
            // pas de soluces de départ, on garde les nouvelles
            this.solutions = otherResult.solutions;
            return;
        }

        //on crée un nouveau tableau pour contenir toutes les solutions
        int[][][][] newSolutions = new int[this.getNbSolutions() + nbSolutionToAdd][][][];

        // on copie les anciennes soluces puis les nouvelles (pas besoin de deep copy ici)
        int solutionIndex = 0; 
        for(int[][][] cellArray : this.solutions ) {
            newSolutions[solutionIndex] = cellArray;
            solutionIndex++;
        }
        for(int[][][] cellArray : otherResult.solutions ) {
            newSolutions[solutionIndex] = cellArray;
            solutionIndex++;
        }
        this.solutions = newSolutions;
    }

    public void addSolution(int[][][] cellArray){
        int currentLength = this.solutions == null ? 0 : this.solutions.length;
        int[][][][] newSolutions = new int[currentLength + 1][][][];
        
        int index = 0;
        while( index <= currentLength - 1) {
            newSolutions[index] = this.solutions[index];
            index++;            
        }
        newSolutions[index] = cellArray;
        this.solutions = newSolutions;
    }

    /**
     * Renvoie l'instance du tableau de solutions
     * utilisé uniquement par la classe de test ! 
     * @return int[][][] : un pointeur vers l'instance originale
     */
    public int[][][][] getSolutionsInstance() {
        return this.solutions;
    }
    
    /** 
     * Revoie une copie de la solution à l'index solutionIndex
     * @param solutionIndex
     * @return int[][][] la solution demandée ou bien null si l'index est out of bounds
     */
    public int[][][] getSolution(int solutionIndex) {
        if(solutionIndex <= this.getNbSolutions() - 1) {
            return Grid.cellArrayDeepCopy(this.solutions[solutionIndex]);
        } 
        return null;
    }

    public int getNbMaxSolutions(){
        return this.nbMaxSolutions;
    }

    public int getNbSolutions(){
        return this.solutions == null ? 0 : this.solutions.length;
    }

    public void incRecursionCounter() {
        this.nbRecursions++;
    }

    public int getRecursionCounter(){
        return this.nbRecursions;
    }

    public boolean isFull(){
        return this.getNbSolutions() >= this.nbMaxSolutions;
    }



    /**
     * Affiche la totalité des solutions
     */
    public void displaySolutions(){
        int solutionIndex = 0;
        if(this.getNbSolutions() == 0) {
            System.err.println("ERREUR : Il n y a pas de solution a cette grille !");
        } else {
            //il y a au moins une soluce, on affiche la/les grilles
            while(solutionIndex <= this.solutions.length - 1){
                System.out.println(MessageFormat.format("Solution N°{0} :", solutionIndex + 1));
                System.out.println(Grid.cellArrayToString(this.solutions[solutionIndex]));
                solutionIndex++;
            }            
        }

        // peu importe qu'il y ait des solutions ou pas, on affiche les stats
        System.out.println(this.getStats());
    }

    private String getStats() {
        String returnString = "Statistiques : ";

        returnString += "\n\t* Nb Recursions : " + this.nbRecursions;
        returnString += "\n\t* Nb Passes : " + this.nbPasses; 
        returnString += "\n\t* Nb Impossible a resoudre : " + this.nbUnsolvableGrids;
        returnString += "\n\t* Nb Abandonnes (> nbPassesMax) : " + this.nbFailedGrids;
        returnString += "\n\t* Passes ms : " + String.format("%.3f", this.nbNanoSeconds / 1000000.0f);

        return returnString;        
    }
}
