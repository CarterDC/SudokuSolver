package utils;

import java.util.Arrays;

public class PassResult {
    public static final char NULL_STATE = 0b0000;
    public static final char IS_DIRTY = 0b0001;
    public static final char HAS_MULTIPLE_CANDIDATES= 0b0010;
    public static final char IS_UNSOLVABLE = 0b0100;

    private char state;
    private int[][][] cellArray;

    public PassResult(){
        this.state = 0b0000;
        this.cellArray = new int[9][9][];
    }

    public PassResult(char state, int[][][] cellArrayCopy){
        this.state = state;
        this.cellArray = cellArrayCopy;
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
        return !this.hasMultipleCandidates();
    }

    public boolean isUnsolvable(){
        return (this.state & IS_UNSOLVABLE) != 0;
    }
    
    public int[][][] getCellArrayInstance(){
        return this.cellArray;
    }

    public int[][][] getCellArrayCopy(){
        return Arrays.copyOf(this.cellArray, this.cellArray.length);
    }

}
