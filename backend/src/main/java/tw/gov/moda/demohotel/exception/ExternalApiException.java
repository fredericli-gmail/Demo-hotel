package tw.gov.moda.demohotel.exception;

/**
 * 繁體中文註解：表示呼叫外部 API 時發生錯誤的共用例外。
 */
public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
