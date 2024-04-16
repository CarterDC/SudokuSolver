package utils;

/**
 * Classe qui étend les fonctionnalité de RuntimeException pour ajouter une gestion des arguments multiples.
 * <p>
 * Certain messages d'erreur à destination des utilisateurs nécessitent plusieurs informations
 * une instance de CustomException peut donc être crée avec un tableau de Strings en argument.
 */
public class CustomException extends RuntimeException {
    
	/**
	 * Rajouté pour faire plaisir à eclipse
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Les multiples arguments qui peuvent servir a identifier précisément une erreur
     */
	public String[] errArgs;

    public CustomException(String message){
        super(message);
        this.errArgs = new String[]{message};
    }

    public CustomException(String[] errArgs) {
        super(String.format("Erreur custom avec paramètres suivants : %s", String.join(", " , errArgs)));
        this.errArgs = errArgs == null ? new String[0] : errArgs;
    }
}
