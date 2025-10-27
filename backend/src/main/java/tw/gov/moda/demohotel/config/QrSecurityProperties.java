package tw.gov.moda.demohotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 繁體中文註解：管理 QR Code 解析所需的金鑰設定，來源為 application.yml。
 */
@Component
@ConfigurationProperties(prefix = "security.qr")
public class QrSecurityProperties {

  /**
   * 繁體中文註解：ECC 公鑰，用於驗證方加密。
   */
  private String eccPublicKey;

  /**
   * 繁體中文註解：ECC 私鑰，用於解密資料。
   */
  private String eccPrivateKey;

  /**
   * 繁體中文註解：HMAC 檢核用金鑰。
   */
  private String hmacKey;

  /**
   * 繁體中文註解：TOTP 驗證用金鑰。
   */
  private String totpKey;

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
