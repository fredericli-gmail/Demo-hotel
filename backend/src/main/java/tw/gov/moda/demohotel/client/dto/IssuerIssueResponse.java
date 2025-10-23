package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 繁體中文註解：對應 DWVC-101 的回應資料。
 */
public class IssuerIssueResponse {

    /**
     * 繁體中文註解：交易序號，後續查詢狀態時使用。
     */
    private String transactionId;

    /**
     * 繁體中文註解：QR Code 的 Base64 圖片字串。
     */
    private String qrCode;

    /**
     * 繁體中文註解：可直接觸發 APP 的 Deep Link。
     */
    private String deepLink;

    /**
     * 繁體中文註解：撤銷卡片相關警示資訊。
     */
    private IssuerIssueWarnings warnings = new IssuerIssueWarnings();

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public IssuerIssueWarnings getWarnings() {
        return warnings;
    }

    public void setWarnings(IssuerIssueWarnings warnings) {
        if (warnings == null) {
            this.warnings = new IssuerIssueWarnings();
        } else {
            this.warnings = warnings;
        }
    }

    /**
     * 繁體中文註解：封裝 API 回傳的警示資訊。
     */
    public static class IssuerIssueWarnings {

        private List<String> statusRevoke = new ArrayList<String>();
        private List<String> cidNotFound = new ArrayList<String>();

        public List<String> getStatusRevoke() {
            return statusRevoke;
        }

        public void setStatusRevoke(List<String> statusRevoke) {
            if (statusRevoke == null) {
                this.statusRevoke = new ArrayList<String>();
            } else {
                this.statusRevoke = statusRevoke;
            }
        }

        public List<String> getCidNotFound() {
            return cidNotFound;
        }

        public void setCidNotFound(List<String> cidNotFound) {
            if (cidNotFound == null) {
                this.cidNotFound = new ArrayList<String>();
            } else {
                this.cidNotFound = cidNotFound;
            }
        }
    }
}
