package tw.gov.moda.demohotel.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：駕照驗證結果回應物件。
 */
public class DriverLicenseVerificationResultResponse {

    private String transactionId;
    private boolean verified;
    private String status;
    private String message;
    private LocalDateTime lastUpdated;
    private List<DriverLicenseClaim> claims = new ArrayList<DriverLicenseClaim>();

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<DriverLicenseClaim> getClaims() {
        return claims;
    }

    public void setClaims(List<DriverLicenseClaim> claims) {
        if (claims == null) {
            this.claims = new ArrayList<DriverLicenseClaim>();
        } else {
            this.claims = claims;
        }
    }
}
