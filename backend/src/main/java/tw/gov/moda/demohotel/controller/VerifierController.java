package tw.gov.moda.demohotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.gov.moda.demohotel.client.dto.VerifierDeepLinkResponse;
import tw.gov.moda.demohotel.client.dto.VerifierQrcodeResponse;
import tw.gov.moda.demohotel.client.dto.VerifierResultRequest;
import tw.gov.moda.demohotel.client.dto.VerifierResultResponse;
import tw.gov.moda.demohotel.service.VerifierService;

/**
 * 繁體中文註解：驗證端相關操作的 REST 介面。
 */
@RestController
@RequestMapping("/api/verifier")
public class VerifierController {

    private final VerifierService verifierService;

    public VerifierController(VerifierService verifierService) {
        this.verifierService = verifierService;
    }

    /**
     * 繁體中文註解：產生驗證用 QR Code。
     *
     * @param ref           VP 範本代碼
     * @param transactionId 交易序號
     * @param enableCallback 是否啟用 callback （預設 false）
     * @return VerifierQrcodeResponse
     */
    @GetMapping("/qrcode")
    public ResponseEntity<VerifierQrcodeResponse> createQrcode(@RequestParam("ref") String ref,
                                                               @RequestParam("transactionId") String transactionId,
                                                               @RequestParam(value = "enableCallback", defaultValue = "false") boolean enableCallback) {
        VerifierQrcodeResponse response = verifierService.createVerificationQrcode(ref, transactionId, enableCallback);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：查詢驗證結果。
     *
     * @param request 查詢請求
     * @return VerifierResultResponse
     */
    @PostMapping("/result")
    public ResponseEntity<VerifierResultResponse> queryResult(@RequestBody VerifierResultRequest request) {
        VerifierResultResponse response = verifierService.queryVerificationResult(request.getTransactionId());
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：取得 deep link。
     *
     * @param vpUid VP 模板代碼
     * @return VerifierDeepLinkResponse
     */
    @GetMapping("/deeplink/{vpUid}")
    public ResponseEntity<VerifierDeepLinkResponse> requestDeepLink(@PathVariable("vpUid") String vpUid) {
        VerifierDeepLinkResponse response = verifierService.requestDeepLink(vpUid);
        return ResponseEntity.ok(response);
    }
}
