package tw.gov.moda.demohotel.dto;

import java.time.LocalDateTime;

/**
 * 繁體中文註解：駕照驗證初始化回應。
 */
public class DriverLicenseVerificationInitResponse {

    private String transactionId;
    private String qrcodeImage;
    private String authUri;
    private int expiresIn;
    private LocalDateTime generatedAt;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getQrcodeImage() {
        return qrcodeImage;
    }

    public void setQrcodeImage(String qrcodeImage) {
        this.qrcodeImage = qrcodeImage;
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
