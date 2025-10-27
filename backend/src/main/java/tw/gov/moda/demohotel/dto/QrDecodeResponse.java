package tw.gov.moda.demohotel.dto;

import java.util.Map;

/**
 * 繁體中文註解：回傳給前端的 QR Code 解析結果。
 */
public class QrDecodeResponse {

  private final boolean success;
  private final String message;
  private final String decryptedPayload;
  private final Map<String, Object> data;

  public QrDecodeResponse(boolean success, String message, String decryptedPayload, Map<String, Object> data) {
    this.success = success;
    this.message = message;
    this.decryptedPayload = decryptedPayload;
    this.data = data;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public String getDecryptedPayload() {
    return decryptedPayload;
  }

  public Map<String, Object> getData() {
    return data;
  }
}
