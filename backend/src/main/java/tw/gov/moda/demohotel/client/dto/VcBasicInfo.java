package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：表示查詢結果中的 VC 基本資訊。
 */
public class VcBasicInfo {

    private String cid;
    private String vcUid;
    private String issuranceDate;
    private String expirationDate;
    private String credentialStatus;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getIssuranceDate() {
        return issuranceDate;
    }

    public void setIssuranceDate(String issuranceDate) {
        this.issuranceDate = issuranceDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCredentialStatus() {
        return credentialStatus;
    }

    public void setCredentialStatus(String credentialStatus) {
        this.credentialStatus = credentialStatus;
    }
}
