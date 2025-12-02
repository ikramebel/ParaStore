package store.example.store.exception;


/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée
 * 
 * Cette exception est utilisée dans les services lorsqu'une entité
 * (utilisateur, produit, commande, etc.) n'existe pas en base de données.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur
     * 
     * @param message Message décrivant l'erreur
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause
     * 
     * @param message Message décrivant l'erreur
     * @param cause Cause de l'exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
