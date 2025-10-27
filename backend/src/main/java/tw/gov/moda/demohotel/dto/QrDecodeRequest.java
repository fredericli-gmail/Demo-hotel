package tw.gov.moda.demohotel.dto;

/**
 * 繁體中文註解：前端送入 QR Code 加密資料與可選金鑰覆寫設定。
 */
public class QrDecodeRequest {

  private String encryptedData;
  private String eccPublicKey;
  private String eccPrivateKey;
  private String hmacKey;
  private String totpKey;

  public String getEncryptedData() {
    return encryptedData;
  }

  public void setEncryptedData(String encryptedData) {
    this.encryptedData = encryptedData;
  }

  public String getEccPublicKey() {
    return eccPublicKey;
  }

  public void setEccPublicKey(String eccPublicKey) {
    this.eccPublicKey = eccPublicKey;
  }

  public String getEccPrivateKey() {
    return eccPrivateKey;
  }

  public void setEccPrivateKey(String eccPrivateKey) {
    this.eccPrivateKey = eccPrivateKey;
  }

  public String getHmacKey() {
    return hmacKey;
  }

  public void setHmacKey(String hmacKey) {
    this.hmacKey = hmacKey;
  }

  public String getTotpKey() {
    return totpKey;
  }

  public void setTotpKey(String totpKey) {
    this.totpKey = totpKey;
  }
}
