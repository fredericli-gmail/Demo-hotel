package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：對應 DWVP-02-101 的回應資料。
 */
public class VerifierDeepLinkResponse {

    private String code;
    private String message;
    private VerifierDeepLinkResponseData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VerifierDeepLinkResponseData getData() {
        return data;
    }

    public void setData(VerifierDeepLinkResponseData data) {
        this.data = data;
    }
}
