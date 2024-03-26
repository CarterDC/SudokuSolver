package utils;

import java.text.MessageFormat;

public class SolveResult {
    public static final int MAX_PASSES = 81;
    
    private int nbPasses = 0;
    private int nbRecursions = 1;
    private int nbMaxSolutions = 0;
    private int nbUnsolvable = 0;
    private int nbFailed = 0;
    private long totalNanosec = 0;
    private boolean needsRecursion = false;

    private int[][][][] solutions;

    public SolveResult(int nbMaxSolutions){
        this.nbMaxSolutions = nbMaxSolutions;
    }

    public SolveResult(int nbMaxSolutions, PassResult passResult, int nbPasses, long passTime){
        this.nbMaxSolutions = nbMaxSolutions;
        this.nbPasses = nbPasses;
        this.totalNanosec = passTime;

        if(passResult.isUnsolvable()){
            this.setNbUnsolvable(1);
            return;
        }
        if(nbPasses >= MAX_PASSES){
            this.setNbFailed(1);
            return;
        }
        //todo : check s'il faut vérif la qtté de soluces avant
        if(passResult.isSolved()){
            this.addSolution(passResult.getCellArrayCopy());
            return;
        }
        if(passResult.hasMultipleCandidates()){
            //todo peut être vérifs ici
            this.needsRecursion = true;            
        }
    }

    public void combineResults(SolveResult otherResult) {

        this.nbMaxSolutions = otherResult.nbMaxSolutions;

        this.nbRecursions += otherResult.nbRecursions;
        this.nbPasses += otherResult.nbPasses;
        this.nbUnsolvable += otherResult.nbUnsolvable;
        this.nbFailed += otherResult.nbFailed;
        this.totalNanosec += otherResult.totalNanosec;
        // ! On ne copie pas le flag "needsRecursion"

        if(otherResult.solutions == null) { return;}
        int solutionIndex = 0;        
        while(solutionIndex <= otherResult.solutions.length - 1) {
            // todo faire la copie ici, en une seule fois
            this.addSolution(otherResult.solutions[solutionIndex]);
            solutionIndex++;
        }
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

    public void setNbUnsolvable(int newValue){
        this.nbUnsolvable = newValue;
    }

    public void setNbFailed(int newValue){
        this.nbFailed = newValue;
    }

    public int getNbSolutions(){
        return this.solutions == null ? 0 : this.solutions.length;
    }

    public int getNbMaxSolutions(){
        return this.nbMaxSolutions;
    }

    public boolean isFull(){
        return this.getNbSolutions() >= this.nbMaxSolutions;
    }

    public void incNbPasses(){
        this.nbPasses++;
    }

    public void setNbPasses(int newValue){
        this.nbPasses = newValue;
    }

    public int getNbPasses(){
        return this.nbPasses;
    }

    public void setTotalNanosec(long newValue){
        this.totalNanosec = newValue;
    }

    public long getTotalNanosec(){
        return this.totalNanosec;
    }

    public boolean needsRecursion(){
        return this.needsRecursion;
    }

    public void displayStats(){
        System.out.println(MessageFormat.format("Nb Recursions : {0}", this.nbRecursions - 1));
        System.out.println(MessageFormat.format("Nb Passes : {0}", this.nbPasses));
        System.out.println(MessageFormat.format("Nb Impossible a resoudre : {0}", this.nbUnsolvable));
        System.out.println(MessageFormat.format("Nb Abandonnes (> nbPassesMax) : {0}", this.nbFailed));

        System.out.println(MessageFormat.format(
            "Total ms : {0}", String.format("%.3f", this.totalNanosec / 1000000.0f)));
    }

    public void displayGrids(){
        int solutionIndex = 0;
        if(this.solutions == null) {
            System.err.println("Il n y a pas de solution a cette grille !");
            return;
        }
        while(solutionIndex <= this.solutions.length - 1){
            System.out.println(MessageFormat.format("Solution N°{0} :", solutionIndex + 1));
            Grid.displayGrid(this.solutions[solutionIndex]);
            solutionIndex++;
        }
    }


}
