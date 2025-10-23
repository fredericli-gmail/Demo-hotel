package tw.gov.moda.demohotel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 繁體中文註解：集中處理例外狀況，對外提供統一的錯誤格式。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<String> handleExternalApiException(ExternalApiException ex) {
        LOGGER.error("外部 API 呼叫失敗", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("外部服務暫時無法使用：" + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        LOGGER.error("未預期錯誤", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統發生未預期錯誤，請聯絡維運人員");
    }
}
