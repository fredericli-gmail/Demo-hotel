package tw.gov.moda.demohotel.exception;

/**
 * 繁體中文註解：代表憑證尚未完成產出，需稍後再試。
 */
public class CredentialPendingException extends RuntimeException {

    public CredentialPendingException(String message) {
        super(message);
    }

    public CredentialPendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
