package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：對應 DWVC-301 的回應資料。
 */
public class IssuerCredentialStatusChangeResponse {

    private String credentialStatus;

    public String getCredentialStatus() {
        return credentialStatus;
    }

    public void setCredentialStatus(String credentialStatus) {
        this.credentialStatus = credentialStatus;
    }
}
