package utils;

/**
 * Classe qui regroupe les statistiques d'execution du programme.
 * <p>
 * Une nouvelle instance de Stats est crée avec chaque nouvelle instance de @see SolveResult
 * 
 */
public class Stats {

    /**
     * Compteur du nombre de récursions faites pour trouver les solutions demandées
     * On ne compte pas la première occurence comme étant une récursion
     *  puisqu'elle ne s'est executée qu'une fois, d'où le départ à -1
     */
    private static int nbRecursions = -1;

    private int nbPasses = 0;
    private int nbUnsolvableGrids = 0;
    private int nbFailedGrids = 0;
    private long nbNanoSeconds = 0;

    public Stats(){
        Stats.nbRecursions++;
    }

    public void aggregate(Stats otherStats){
        this.nbPasses += otherStats.getNbPasses();
        this.nbUnsolvableGrids += otherStats.getNbUnsolvableGrids();
        this.nbFailedGrids += otherStats.getNbFailedGrids();
        this.nbNanoSeconds += otherStats.getNbNanoSeconds();
    }

    public String toString(){
        String returnString = "Statistiques : ";

        returnString += "\n\t* Nb Recursions : " + Stats.nbRecursions;
        returnString += "\n\t* Nb Passes : " + this.nbPasses; 
        returnString += "\n\t* Nb Impossible a resoudre : " + this.nbUnsolvableGrids;
        returnString += "\n\t* Nb Abandonnes (> nbPassesMax) : " + this.nbFailedGrids;
        returnString += "\n\t* Total ms : " + String.format("%.3f", this.nbNanoSeconds / 1000000.0f);

        return returnString;
    }

    // getters
    public int getNbPasses() {
        return nbPasses;
    }

    public int getNbRecursions() {
        return Stats.nbRecursions;
    }

    public int getNbUnsolvableGrids() {
        return nbUnsolvableGrids;
    }

    public int getNbFailedGrids() {
        return nbFailedGrids;
    }

    public long getNbNanoSeconds() {
        return nbNanoSeconds;
    }

    // "Setters" par incrément
    public void incNbPasses() {
        this.nbPasses++;
    }

    public void incNbUnsolvable() {
        this.nbUnsolvableGrids++;
    }

    public void incNbFailed() {
        this.nbFailedGrids++;
    }

    public void addNbPasses(int nbToAdd) {
        this.nbPasses += nbToAdd;
    }

    public void addNanoSeconds(long nbToAdd) {
        this.nbNanoSeconds += nbToAdd;
    }
}
