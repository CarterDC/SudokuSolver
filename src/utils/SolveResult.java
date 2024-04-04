package utils;

import java.text.MessageFormat;

/**
 * Classe qui compile les solutions trouvées ainsi que les statistiques d'execution
 * <p>
 * 
 */
public class SolveResult {
    
    private int nbMaxSolutions = 0;
    private Stats stats;

    private boolean needsRecursion = false;

    private int[][][][] solutions;

    public SolveResult(int nbMaxSolutions){
        this.nbMaxSolutions = nbMaxSolutions;
        this.stats = new Stats();
    }

    public SolveResult(int nbMaxSolutions, PassResult passResult, long passTime){
        this.nbMaxSolutions = nbMaxSolutions;
        this.stats = new Stats();

        this.stats.addNbPasses(passResult.getNbPasses()); 
        this.stats.addNanoSeconds(passTime);

        if(passResult.isUnsolvable()){
            this.stats.incNbUnsolvable();
            return;
        }
        if(passResult.getNbPasses() >= PassResult.MAX_PASSES){
            this.stats.incNbFailed();
            return;
        }

        //TODO : check s'il faut vérif la qtté de soluces avant
        if(passResult.isSolved()){
            this.addSolution(passResult.getCellArrayCopy());
            return;
        }
        if(passResult.hasMultipleCandidates()){
            //TODO peut être vérifs ici
            this.needsRecursion = true;            
        }
    }

    public void aggregate(SolveResult otherResult) {

        this.nbMaxSolutions = otherResult.nbMaxSolutions; // TODO : virer ça à terme
        this.stats.aggregate(otherResult.getStats());
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

    public int getNbSolutions(){
        return this.solutions == null ? 0 : this.solutions.length;
    }

    public Stats getStats(){
        return this.stats;
    }

    public boolean isFull(){
        return this.getNbSolutions() >= this.nbMaxSolutions;
    }

    public boolean needsRecursion(){
        return this.needsRecursion;
    }

    /**
     * Affiche la totalité des solutions
     */
    public void displaySolutions(){ // TODO : pê remplacer par un toString ?
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
        System.out.println("" + this.stats);
    }
}
