package tw.gov.moda.demohotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.gov.moda.demohotel.dto.DriverLicenseVerificationInitResponse;
import tw.gov.moda.demohotel.dto.DriverLicenseVerificationResultResponse;
import tw.gov.moda.demohotel.service.DriverLicenseVerificationService;

/**
 * 繁體中文註解：駕照 VP 授權驗證相關操作。
 */
@RestController
@RequestMapping("/api/vp/driver-license")
public class DriverLicenseVerificationController {

    private final DriverLicenseVerificationService driverLicenseVerificationService;

    public DriverLicenseVerificationController(DriverLicenseVerificationService driverLicenseVerificationService) {
        this.driverLicenseVerificationService = driverLicenseVerificationService;
    }

    /**
     * 繁體中文註解：產生驗證 QR Code。
     *
     * @return DriverLicenseVerificationInitResponse
     */
    @PostMapping("/qrcode")
    public ResponseEntity<DriverLicenseVerificationInitResponse> generateQrcode() {
        DriverLicenseVerificationInitResponse response = driverLicenseVerificationService.createVerification();
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：取得驗證結果。
     *
     * @param transactionId 交易序號
     * @return DriverLicenseVerificationResultResponse
     */
    @GetMapping("/result/{transactionId}")
    public ResponseEntity<DriverLicenseVerificationResultResponse> queryResult(@PathVariable("transactionId") String transactionId) {
        DriverLicenseVerificationResultResponse response = driverLicenseVerificationService.queryVerification(transactionId);
        return ResponseEntity.ok(response);
    }
}
