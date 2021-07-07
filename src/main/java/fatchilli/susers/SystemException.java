package fatchilli.susers;

/**
 * Marks there is an unpredictable and (most likely) unrecoverable exception
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
