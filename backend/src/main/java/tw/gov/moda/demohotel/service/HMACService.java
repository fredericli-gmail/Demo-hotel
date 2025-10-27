package tw.gov.moda.demohotel.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 繁體中文註解：提供 HMAC-SHA256 金鑰產生與訊息驗證碼計算功能。
 */
@Service
public class HMACService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HMACService.class);

  /**
   * 繁體中文註解：產生 256 位元 HMAC 金鑰並回傳 Base64 編碼字串。
   */
  public String generateKey() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      keyGenerator.init(256);
      SecretKey secretKey = keyGenerator.generateKey();
      return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("HMAC 金鑰產生失敗", exception);
    }
  }

  /**
   * 繁體中文註解：使用 Base64 編碼金鑰計算資料的 HMAC 值並回傳 Base64 字串。
   */
  public String calculateHmac(String data, String base64Key) throws Exception {
    LOGGER.info("開始計算 HMAC，資料長度={}，金鑰長度={}", data != null ? data.length() : 0, base64Key != null ? base64Key.length() : 0);
    byte[] keyBytes = Base64.getDecoder().decode(base64Key);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(keySpec);
    byte[] macBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(macBytes);
  }
}
