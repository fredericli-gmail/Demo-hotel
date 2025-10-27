package tw.gov.moda.demohotel.service;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.gov.moda.demohotel.config.QrSecurityProperties;
import tw.gov.moda.demohotel.dto.QrDecodeRequest;
import tw.gov.moda.demohotel.dto.QrDecodeResponse;

/**
 * 繁體中文註解：整合 ECC、HMAC 與 TOTP 驗證流程，將加密資料還原成明文。
 */
@Service
public class QrCodeCryptoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeCryptoService.class);

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ECCService eccService;
  private final HMACService hmacService;
  private final TOTPService totpService;
  private final QrSecurityProperties qrSecurityProperties;

  public QrCodeCryptoService(
      ECCService eccService,
      HMACService hmacService,
      TOTPService totpService,
      QrSecurityProperties qrSecurityProperties) {
    this.eccService = eccService;
    this.hmacService = hmacService;
    this.totpService = totpService;
    this.qrSecurityProperties = qrSecurityProperties;
  }

  /**
   * 繁體中文註解：解析加密 QR Code，並進行 TOTP、HMAC 驗證。
   */
  public QrDecodeResponse decode(QrDecodeRequest request) {
    if (request == null || !StringUtils.hasText(request.getEncryptedData())) {
      return new QrDecodeResponse(false, "請提供加密資料", null, null);
    }

    String eccPrivateKey = chooseKey(request.getEccPrivateKey(), qrSecurityProperties.getEccPrivateKey());
    String hmacKey = chooseKey(request.getHmacKey(), qrSecurityProperties.getHmacKey());
    String totpKey = chooseKey(request.getTotpKey(), qrSecurityProperties.getTotpKey());

    if (!StringUtils.hasText(eccPrivateKey)) {
      return new QrDecodeResponse(false, "系統缺少 ECC 私鑰設定，無法解密資料", null, null);
    }

    try {
      String encryptedBlock = request.getEncryptedData().trim();
      String expectedHmac = null;

      if (isLikelyJson(encryptedBlock)) {
        JsonNode encryptedNode = objectMapper.readTree(encryptedBlock);
        if (encryptedNode.has("d") && encryptedNode.has("h")) {
          expectedHmac = encryptedNode.get("h").asText();
          encryptedBlock = encryptedNode.get("d").asText();
        } else {
          return new QrDecodeResponse(false, "加密資料格式錯誤：缺少必要欄位", null, null);
        }
      }

      String decryptedRaw = eccService.decrypt(encryptedBlock, eccPrivateKey);
      String normalizedPayload = normalizeJson(decryptedRaw);

      JsonNode payloadNode = parseJson(normalizedPayload);
      if (payloadNode == null) {
        return new QrDecodeResponse(false, "解密成功但資料格式非 JSON，無法繼續驗證", decryptedRaw, null);
      }

      if (!payloadNode.has("totp")) {
        return new QrDecodeResponse(false, "解密資料缺少 TOTP 欄位", normalizedPayload, toMap(payloadNode));
      }

      if (!StringUtils.hasText(totpKey)) {
        return new QrDecodeResponse(false, "系統缺少 TOTP 金鑰設定，無法驗證一次性密碼", normalizedPayload, toMap(payloadNode));
      }

      String totpValue = payloadNode.get("totp").asText();
      if (!totpService.verify(totpValue, totpKey)) {
        return new QrDecodeResponse(false, "TOTP 驗證失敗：一次性密碼無效或已過期", normalizedPayload, toMap(payloadNode));
      }

      if (expectedHmac != null) {
        if (!StringUtils.hasText(hmacKey)) {
          return new QrDecodeResponse(false, "偵測到 HMAC 但尚未設定金鑰，無法驗證", normalizedPayload, toMap(payloadNode));
        }
        String calculatedHmac = hmacService.calculateHmac(decryptedRaw, hmacKey);
        if (!expectedHmac.equals(calculatedHmac)) {
          return new QrDecodeResponse(false, "HMAC 驗證失敗：資料可能遭竄改", normalizedPayload, toMap(payloadNode));
        }
      }

      return new QrDecodeResponse(true, "驗證成功", normalizedPayload, toMap(payloadNode));
    } catch (SecurityException securityException) {
      LOGGER.error("解密過程發生安全性錯誤", securityException);
      return new QrDecodeResponse(false, "解密失敗：MAC 驗證未通過", null, null);
    } catch (Exception exception) {
      LOGGER.error("解析 QR Code 失敗", exception);
      return new QrDecodeResponse(false, "解析過程發生錯誤：" + exception.getMessage(), null, null);
    }
  }

  private String chooseKey(String overrideKey, String configuredKey) {
    if (StringUtils.hasText(overrideKey)) {
      return overrideKey;
    }
    return configuredKey;
  }

  private boolean isLikelyJson(String value) {
    return value.startsWith("{") && value.endsWith("}");
  }

  private String normalizeJson(String payload) {
    try {
      JsonNode node = objectMapper.readTree(payload);
      return objectMapper.writeValueAsString(node);
    } catch (JsonProcessingException exception) {
      return payload;
    }
  }

  private JsonNode parseJson(String payload) {
    try {
      return objectMapper.readTree(payload);
    } catch (JsonProcessingException exception) {
      LOGGER.warn("解密後資料不是合法 JSON：{}", exception.getMessage());
      return null;
    }
  }

  private Map<String, Object> toMap(JsonNode jsonNode) {
    return objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() { });
  }
}
