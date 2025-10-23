package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：對應 DWVC-201 的回應資料。
 */
public class IssuerVcQueryResponse {

    /**
     * 繁體中文註解：JWT 格式的憑證資料。
     */
    private String credential;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
