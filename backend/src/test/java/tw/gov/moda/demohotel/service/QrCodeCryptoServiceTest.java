package tw.gov.moda.demohotel.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import tw.gov.moda.demohotel.config.QrSecurityProperties;
import tw.gov.moda.demohotel.dto.QrDecodeRequest;
import tw.gov.moda.demohotel.dto.QrDecodeResponse;

/**
 * 繁體中文註解：驗證 QrCodeCryptoService 解碼流程的核心行為。
 */
public class QrCodeCryptoServiceTest {

  private ECCService eccService;
  private HMACService hmacService;
  private TOTPService totpService;
  private QrSecurityProperties qrSecurityProperties;
  private QrCodeCryptoService qrCodeCryptoService;

  @BeforeEach
  void setUp() {
    eccService = Mockito.mock(ECCService.class);
    hmacService = Mockito.mock(HMACService.class);
    totpService = Mockito.mock(TOTPService.class);
    qrSecurityProperties = new QrSecurityProperties();
    qrSecurityProperties.setEccPrivateKey("private-key");
    qrSecurityProperties.setHmacKey("hmac-key");
    qrSecurityProperties.setTotpKey("totp-key");
    qrCodeCryptoService = new QrCodeCryptoService(eccService, hmacService, totpService, qrSecurityProperties);
  }

  @Test
  void decodeShouldReturnSuccessWhenVerificationPasses() throws Exception {
    QrDecodeRequest request = new QrDecodeRequest();
    request.setEncryptedData("{\"d\":\"cipher-text\",\"h\":\"expected-hmac\"}");

    String decryptedPayload = "{\"totp\":\"123456\",\"room\":\"901\"}";
    when(eccService.decrypt("cipher-text", "private-key")).thenReturn(decryptedPayload);
    when(totpService.verify("123456", "totp-key")).thenReturn(true);
    when(hmacService.calculateHmac(decryptedPayload, "hmac-key")).thenReturn("expected-hmac");

    QrDecodeResponse response = qrCodeCryptoService.decode(request);

    assertTrue(response.isSuccess());
  }

  @Test
  void decodeShouldReturnFailureWhenTotpInvalid() throws Exception {
    QrDecodeRequest request = new QrDecodeRequest();
    request.setEncryptedData("{\"d\":\"cipher-text\",\"h\":\"expected-hmac\"}");

    String decryptedPayload = "{\"totp\":\"000000\",\"room\":\"901\"}";
    when(eccService.decrypt("cipher-text", "private-key")).thenReturn(decryptedPayload);
    when(totpService.verify("000000", "totp-key")).thenReturn(false);
    when(hmacService.calculateHmac(decryptedPayload, "hmac-key")).thenReturn("expected-hmac");

    QrDecodeResponse response = qrCodeCryptoService.decode(request);

    assertFalse(response.isSuccess());
  }
}
