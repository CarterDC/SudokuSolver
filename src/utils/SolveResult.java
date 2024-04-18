package utils;

import java.text.MessageFormat;

/**
 * Classe qui compile les solutions trouvées ainsi que les statistiques d'execution
 * <p>
 * 
 */
public class SolveResult {
    
    private int[][][][] solutions;
    private PassResult currentPassResult; // l'embranchement exploré en cours

    // le nb de soluces demandé par l'utilisateur    
    private int nbMaxSolutions = 0; 

    // stats
    private int nbRecursions = -1;
    private int nbPasses = 0;
    private int nbUnsolvableGrids = 0;
    private int nbFailedGrids = 0;
    private long nbNanoSeconds = 0;

    private long startingTime; 


    public SolveResult(int[][][] currentCellArray, int nbMaxSolutions){
        this.currentPassResult = new PassResult(currentCellArray);
        this.nbMaxSolutions = nbMaxSolutions;
    }

    /** 
     * Ajoute une nouvelle solution en recréant un nouveau tableau.
     * TODO : pê utiliser une liste récursive plutot qu'un array ? 
     * @param cellArray
     */
    public void addSolution(int[][][] cellArray){
        int currentLength = this.solutions == null ? 0 : this.solutions.length;
        // crée un nouveau tableau plus grand 
        int[][][][] newSolutions = new int[currentLength + 1][][][];
        // recopie les solutions déjà trouvées
        int index = 0;
        while( index <= currentLength - 1) {
            newSolutions[index] = this.solutions[index];
            index++;            
        }
        //ajoute la nouvelle solution
        newSolutions[index] = cellArray;
        this.solutions = newSolutions;
    }

    public void startTimer(){
        this.startingTime = System.nanoTime();
    }

    /**
     * Cumule le temps écoulé avec la durée des passes précédentes 
     */
    public void stopTimer() {
        long elapsedTime = System.nanoTime() - this.startingTime;
        this.nbNanoSeconds += elapsedTime;
    }    

    /**
     * Decide si la grille courrante a besoin d'une récursion supplémentaire pour trouver des solutions.
     * <p>
     * Si la grille n'a pas de solution, qu'on a dépassé la profondeur maxi de récursion, qu'on a solutionné la grille
     * ou bien qu'on a notre quota de solutions demandées, il n'y a pas besoin de récursion.
     * On aura besoin de créer un embranchement seulement s'il existe au moins une cellule contenant de multiples candidats
     * 
     * @return boolean 
     */
    public boolean needsRecursion(){
        
        if (this.currentPassResult.isUnsolvable()) { return false;}
        if (this.currentPassResult.getNbPasses() >= Grid.MAX_RECURSION_DEPTH) { return false;}
        if (this.currentPassResult.isSolved()) { return false;}
        if (this.isFull()) { return false;}
        return this.currentPassResult.hasMultipleCandidates() && !this.currentPassResult.isDirty();
    }

    /**
     * Affiche la totalité des solutions trouvées sous forme de grilles dans le terminal
     * <p>
     * Affiche aussi les statistiques d'execution.
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

    /** 
     * Renvoie une chaîne contenant les statistiques d'execution mises en forme pour l'utilisateur
     * @return String
     */
    private String getStats() {
        String returnString = "Statistiques : ";

        returnString += "\n\t* " + this.getNbSolutions() + " solution(s) trouvee(s)";
        returnString += "\n\t* sur " + this.nbMaxSolutions + " solution(s) demandee(s)";
        returnString += "\n\t* grace a " + this.nbPasses + " passes";
        returnString += "\n\t* realisees en " + String.format("%.3f", this.nbNanoSeconds / 1000000.0f) +" millisecondes";
        returnString += "\n\t* reparties sur l exploration de " + this.nbRecursions + " embranchement(s)";
        
        
        returnString += "\n\t* dont " + this.nbUnsolvableGrids + " etaient impossible a resoudre";
        returnString += "\n\t* et dont " + this.nbFailedGrids + " ont ete abandonnes (pour depassement de la profondeur de recursion maximale).";        

        return returnString;        
    }

    /**********************************************
     **            Getters & Setters             **
     **********************************************/

    /**
     * Retourne l'instance du passResult courrant
     * @return
     */
    public PassResult getCurrentPassResult(){
        return this.currentPassResult;
    }

    /**
     * Remplace l'instance de passResult courrante par une nouvelle
     * @param newPassResult
     */
    public void setCurrentPassResult(PassResult newPassResult){
        this.currentPassResult = newPassResult;
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

    public int getNbSolutions(){
        return this.solutions == null ? 0 : this.solutions.length;
    }

    public void incRecursionCounter() {
        this.nbRecursions++;
    }

    public void incNbUnsolvableGrids() {
        this.nbUnsolvableGrids++;
    }

    public void incNbFailedGrids() {
        this.nbFailedGrids++;
    }

    public boolean isFull(){
        return this.getNbSolutions() >= this.nbMaxSolutions;
    }
}
