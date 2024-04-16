package utils;

/**
 * Classe qui symbolise le résultat d'une passe
 * <p>
 * En plus de contenir le cellArray résultant d'une passe,
 *  les instances de passResult contiennent aussi des flags permettant l'analyse 
 * des résultats et la prise de décision.<br>
 * 3 flags sont codés sur un bit et sont réunis au sein d'un meme octet (char) : 
 * <ul>
 *  <li>isDirty : indique que des modifications ont été faites sur la grille durant cette passe</li>
 *  <li>isUnsolvable : indique qu'une cellule dans ce cellArray n'a aucun candidat possible selon les règles</li>
 *  <li>hasMultipleCandidates : indique qu'au moins une cellule n'est pas encore résolue (contient plusieurs valeurs potentielles)</li>
 * </ul>
 * 2 flags sont dérivés des 3 autres 
 * <ul>
 *  <li>isSolved : une passe est résolue si elle n'est pas flagée comme unsolvable, et qu'elle n'est pas flaguée comme ayant encore de multiples candidats (toutes les cellules ne contiennent qu'une valeur au plus)</li>
 *  <li>needsRecursion : Indique s'il est nécessaire de faire une nouvelle passe pour tenter de réduire encore le nombre de candidats</li>
 * </ul>
 */
public class PassResult {
    // constantes
    private static final char NULL_STATE = 0b0000;
    private static final char IS_DIRTY = 0b0001;
    private static final char HAS_MULTIPLE_CANDIDATES= 0b0010;
    private static final char IS_UNSOLVABLE = 0b0100;

    private char state = PassResult.NULL_STATE;
    private int[][][] cellArray;
    private int nbPasses;

    /**
     * Crée une nvelle instance de PassResult avec une copie du cellArray passé en paramètre
     * @param cellArray
     */
    public PassResult(int[][][] cellArray){
        this.cellArray = Grid.cellArrayDeepCopy(cellArray);
        this.nbPasses = 0;
    }

    /**********************************************
     **            Getters & Setters             **
     **********************************************/

    /**
     * Renvoie l'instance originale du cellArray
     * @return int[][][] : un cellArray
     */
    public int[][][] getCellArray(){
        return this.cellArray;
    }

    /**
     * Renvoie une copie du cellArray
     * @return int[][][] : un cellArray
     */
    public int[][][] getCellArrayCopy(){
        return Grid.cellArrayDeepCopy(this.cellArray);
    }
    
    /** 
     * Remplace l'instance de cellArray par une nouvelle
     * @param newCellArray
     */
    public void setCellArray(int[][][] newCellArray){
        this.cellArray = newCellArray;
    }

    public int getNbPasses() {
        return nbPasses;
    }

    public void setNbPasses(int newPasses) {
        this.nbPasses = newPasses;
    }

    // getters pour les flags

    public boolean isDirty(){
        return (this.state & IS_DIRTY) != 0;
    }

    public boolean hasMultipleCandidates(){
        return (this.state & HAS_MULTIPLE_CANDIDATES) != 0;
    }

    public boolean isUnsolvable(){
        return (this.state & IS_UNSOLVABLE) != 0;
    }
    
    public boolean isSolved(){
        return !this.hasMultipleCandidates() && !this.isUnsolvable();
    }
    
    /**
     * Renvoie un boolean basé sur les flags de la passe courante
     *  pour décider si on doit faire une nouvelle passe. 
     * @return boolean
     */
    public boolean needsRecursion() {
        if( this.isUnsolvable() ) { return false;} // cas de figure déjà géré normalement, mais bon
        if( !this.hasMultipleCandidates()) { return false;} // toutes les cellules ont été résolues
        if( !this.isDirty()) { return false; } // s'il n'y a eu aucun changement durant cette passe, il n'y en aura pas pendant la prochaine
        // si la grille n'est pas résolue, qu'il reste des candidats multiples et qu'il y a eu des changements à la dernière passe, on peut faire un tour de plus
        return true;
    }

    // setters pour les flags

    public void setIsDirty(){
        this.state = (char) (this.state | IS_DIRTY);
    }

    public void setHasMultipleCandidates(){
        this.state = (char) (this.state | HAS_MULTIPLE_CANDIDATES);
    }

    public void setIsUnsolvable(){
        this.state = (char) (this.state | IS_UNSOLVABLE);
    }
}
