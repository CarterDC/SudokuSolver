package utils;

import java.text.MessageFormat;

public class Arguments {
        public static final int MAX_SOLUTIONS_DEFAULT = 2;

        public String fileName;
        public int nbMaxSolutions;
        public boolean displayHelp = false;

        public static Arguments parseArguments(String [] args){
            Arguments myArgs = new Arguments();
            
            if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
                myArgs.displayHelp = true;
                return myArgs;
            } else {
                myArgs.fileName = args[0];
                if(args.length > 1) {
                    try{
                        myArgs.nbMaxSolutions = Integer.parseInt(args[1]);    
                    } catch(NumberFormatException e) {
                        String errMsg = MessageFormat.format(
                            "ERREUR : {0} n est pas une valeur valide ! Utilisation du defaut {1}.",
                             args[1], Arguments.MAX_SOLUTIONS_DEFAULT);
                        System.err.println(errMsg);
                    }                    
                }
            }
            return myArgs;
        } 

        public Arguments(){
            this.fileName = "";
            this.nbMaxSolutions = Arguments.MAX_SOLUTIONS_DEFAULT;
        }
    
}
