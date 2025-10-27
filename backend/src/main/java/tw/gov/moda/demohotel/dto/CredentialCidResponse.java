package tw.gov.moda.demohotel.dto;

/**
 * 繁體中文註解：提供前端輪詢憑證 CID 使用的回應格式。
 */
public class CredentialCidResponse {

    private boolean ready;
    private String cid;

    public CredentialCidResponse() {
    }

    public CredentialCidResponse(boolean ready, String cid) {
        this.ready = ready;
        this.cid = cid;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
