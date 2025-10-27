package tw.gov.moda.demohotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.gov.moda.demohotel.dto.QrDecodeRequest;
import tw.gov.moda.demohotel.dto.QrDecodeResponse;
import tw.gov.moda.demohotel.service.QrCodeCryptoService;

/**
 * 繁體中文註解：提供前端解析加密 QR Code 的 API 入口。
 */
@RestController
@RequestMapping("/api/qrcode")
public class QrCodeVerificationController {

  private final QrCodeCryptoService qrCodeCryptoService;

  public QrCodeVerificationController(QrCodeCryptoService qrCodeCryptoService) {
    this.qrCodeCryptoService = qrCodeCryptoService;
  }

  /**
   * 繁體中文註解：接收加密資料並回傳解密與驗證結果。
   */
  @PostMapping("/decode")
  public ResponseEntity<QrDecodeResponse> decode(@RequestBody QrDecodeRequest request) {
    QrDecodeResponse response = qrCodeCryptoService.decode(request);
    return ResponseEntity.ok(response);
  }
}
