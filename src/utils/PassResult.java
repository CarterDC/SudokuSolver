package utils;
// TODO : ajoute max_passes dans le javadoc
/**
 * Classe qui symbolise le résultat d'une passe
 * <p>
 * En plus de contenir le cellArray résultant d'une passe,
 *  les instances de passResult contiennent aussi des flags permettant l'analyse 
 * des résultats et la prise de décision.<br>
 * Ces 3 flags sont codés sur un bit et sont réunis au sein d'un meme char : 
 * <ul>
 *  <li>isDirty : indique que des modifications ont été faites sur la grille durant cette passe</li>
 *  <li>isUnsolvable : indique qu'une cellule dans ce cellArray n'a aucun candidat possible selon les règles</li>
 *  <li>hasMultipleCandidates : indique qu'au moins une cellule n'est pas encore résolue (contient plusieurs valeurs potentielles)</li>
 *  <li>isSolved : flag dérivé. une passe est résolue si elle n'est pas flagée comme unsolvable, et qu'elle n'est pas flaguée comme ayant encore de multiples candidats (toutes les cellules ne contiennent qu'une valeur au plus)</li>
 * </uk>
 */
public class PassResult {
    public static final char NULL_STATE = 0b0000;
    public static final char IS_DIRTY = 0b0001;
    public static final char HAS_MULTIPLE_CANDIDATES= 0b0010;
    public static final char IS_UNSOLVABLE = 0b0100;

    public static final int MAX_PASSES = 81; // certainement bcp trop et inutile

    private char state = PassResult.NULL_STATE;
    private int[][][] cellArray;
    private int nbPasses;

    public PassResult(){
        this.cellArray = new int[9][9][];
        this.nbPasses = 0;
    }

    public PassResult(int[][][] cellArrayCopy){
        this.cellArray = cellArrayCopy;
        this.nbPasses = 0;
    }

    /**
     * Return the cellArray instance
     * @return the instance of the cellArray
     */
    public int[][][] getCellArray(){
        return this.cellArray;
    }

    /**
     * Return a deep copy of the cellArray
     * @return
     */
    public int[][][] getCellArrayCopy(){
        return Grid.cellArrayDeepCopy(cellArray);
    }

    public void setCellArray(int[][][] newCellArray){
        this.cellArray = newCellArray;
    }

    // getters & setters pour les flags

    public void resetState() {
        this.state = PassResult.NULL_STATE;
    }

    public void setDirty(){
        this.state = (char) (this.state | IS_DIRTY);
    }

    public void setHasMultipleCandidates(){
        this.state = (char) (this.state | HAS_MULTIPLE_CANDIDATES);
    }

    public void setIsUnsolvable(){
        this.state = (char) (this.state | IS_UNSOLVABLE);
    }

    public boolean isDirty(){
        return (this.state & IS_DIRTY) != 0;
    }

    public boolean hasMultipleCandidates(){
        return (this.state & HAS_MULTIPLE_CANDIDATES) != 0;
    }

    public boolean isSolved(){
        return !this.hasMultipleCandidates() && !this.isUnsolvable();
    }

    public boolean isUnsolvable(){
        return (this.state & IS_UNSOLVABLE) != 0;
    }

    public boolean needsRecursion() {
        if( this.isUnsolvable() ) { return false;} // cas de figure déjà géré normalement, mais bon
        if( this.hasMultipleCandidates()) { return false;} // toutes les cellules ont été résolues
        if( !this.isDirty()) { return false; } // s'il n'y a eu aucun changement durant cette passe, il n'y en aura pas pendant la prochaine
        if( this.nbPasses >= PassResult.MAX_PASSES ) { return false;} // cas de figure improbable mais il vaut mieux éviter une boucle infinie par principe
        // si la grille n'est pas résolue, qu'il reste des candidats multiples et qu'il y a eu des changements à la dernière passe, on peut faire un tour de plus
        return true;
    }

    // getters
    public int getNbPasses() {
        return nbPasses;
    }

    public void setPasses(int newPasses) {
        this.nbPasses = newPasses;
    }

    // "Setters" par incrément
    public void incNbPasses() {
        this.nbPasses++;
    }
}
