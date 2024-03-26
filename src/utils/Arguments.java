package utils;

import java.text.MessageFormat;

/**
 * Cette classe gère les arguments passés à la fonction main.
 * <p>
 * C'est donc aussi elle qui défini la valeur par défault des arguments optionnels
 */
public class Arguments {

        /**
         * Valeur par default du nb maximum de solutions recherchées
         */
        public static final int MAX_SOLUTIONS_DEFAULT = 2;

        private String fileName; 
        private int nbMaxSolutions;
        private boolean shouldDisplayHelp = false;

        /**
         * Méthode de classe qui parse le tableau de String[] args passé à la function main
         * <p>
         * 
         * @param args
         * @return - Une instance de la classe Argument contenant le résultat du parsing
         */
        public static Arguments parseArguments(String [] args){
            Arguments myArgs = new Arguments();
            
            //on prévoit d'afficher l'aide si l'utilisateur le demande ou s'il n'a passé aucun argument du tout
            if(args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
                myArgs.shouldDisplayHelp = true;
                return myArgs;
            } else {
                // le premier argument obligatoire est le nom de fichier
                // on laisse la suite du programme vérifier si c'est un nom de fichier valide ou pas
                myArgs.fileName = args[0];
                if(args.length > 1) {
                    // l'utilisateur a passé un ou plusieur paramètres optionnels
                    // on ne s'occupe que du premier, qui doit être un int (nbMaxSoluces)
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

        /**
         * Constructeur avec les valeurs par défault
         */
        public Arguments(){
            this.fileName = "";
            this.nbMaxSolutions = Arguments.MAX_SOLUTIONS_DEFAULT;
        }

        /**
         * Getter de la variable privée fileName
         * @return - String : un nom de fichier valide ou pas
         */
        public String getFileName() {
            return this.fileName;
        }

        /**
         * Getter de la variable privée nbMaxSolutions
         * @return - int : le nombre de soluces souhaitées
         */
        public int getNbMaxSolutions() {
            return this.nbMaxSolutions;
        }

        /**
         * Getter de la variable privée shoudlDisplayHelp
         * @return - boolean : est ce qu'il faut afficher l'aide ou pas
         */
        public boolean shouldDisplayHelp() {
            return this.shouldDisplayHelp;
        }
    
}
