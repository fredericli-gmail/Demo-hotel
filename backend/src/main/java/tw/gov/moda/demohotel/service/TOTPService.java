package tw.gov.moda.demohotel.service;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

/**
 * 繁體中文註解：實作 RFC 6238 的 TOTP 功能，提供金鑰產生、一次性密碼產生與驗證。
 */
@Service
public class TOTPService {

  private static final int KEY_SIZE = 256;
  private static final int TOTP_LENGTH = 6;
  private static final int PERIOD_SECONDS = 60;
  private static final int TIME_OFFSET_SECONDS = 30;

  /**
   * 繁體中文註解：產生 Base64 編碼的 TOTP 金鑰。
   */
  public String generateKey() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      keyGenerator.init(KEY_SIZE);
      SecretKey secretKey = keyGenerator.generateKey();
      return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("TOTP 金鑰產生失敗", exception);
    }
  }

  /**
   * 繁體中文註解：驗證輸入的一次性密碼是否有效，允許前後一個時間區間的偏移量。
   */
  public boolean verify(String totpCode, String base64Key) {
    try {
      for (int offset = -1; offset <= 1; offset++) {
        String candidate = generateTotp(base64Key, offset);
        if (candidate.equals(totpCode)) {
          return true;
        }
      }
      return false;
    } catch (Exception exception) {
      return false;
    }
  }

  /**
   * 繁體中文註解：產生當前時間的一次性密碼。
   */
  public String generate(String base64Key) {
    return generateTotp(base64Key, 0);
  }

  private String generateTotp(String base64Key, int timeOffset) {
    try {
      long currentSeconds = Instant.now().getEpochSecond();
      long timeStep = (currentSeconds + TIME_OFFSET_SECONDS) / PERIOD_SECONDS + timeOffset;

      byte[] timeBytes = ByteBuffer.allocate(Long.BYTES).putLong(timeStep).array();
      byte[] keyBytes = Base64.getDecoder().decode(base64Key);

      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(keyBytes, "HmacSHA256"));
      byte[] hash = mac.doFinal(timeBytes);

      int offset = hash[hash.length - 1] & 0x0f;
      int binary = ((hash[offset] & 0x7f) << 24)
          | ((hash[offset + 1] & 0xff) << 16)
          | ((hash[offset + 2] & 0xff) << 8)
          | (hash[offset + 3] & 0xff);

      int otp = binary % (int) Math.pow(10, TOTP_LENGTH);
      return String.format("%0" + TOTP_LENGTH + "d", otp);
    } catch (NoSuchAlgorithmException | InvalidKeyException exception) {
      throw new IllegalStateException("TOTP 產生失敗", exception);
    }
  }
}
