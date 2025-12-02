package store.example.store.exception;



/**
 * Exception levée lors d'erreurs d'autorisation
 * 
 * Cette exception est utilisée lorsqu'un utilisateur tente d'accéder
 * à une ressource pour laquelle il n'a pas les permissions nécessaires.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur
     * 
     * @param message Message décrivant l'erreur
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause
     * 
     * @param message Message décrivant l'erreur
     * @param cause Cause de l'exception
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}

