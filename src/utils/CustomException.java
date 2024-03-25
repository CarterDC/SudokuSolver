package utils;

public class CustomException extends RuntimeException {
    
	/**
	 * Rajouté pour faire plaisir à eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	
	public String[] myArgs;

    public CustomException(String message){
        //
        super(message);
        this.myArgs = new String[]{message};
    }

    public CustomException(String[] args) {
        // TODO : tester cette version !
        super(String.format("Erreur custom avec paramètres suivants : %s", String.join(", " , args)));
        this.myArgs = args == null ? new String[0] : args;
    }
}
