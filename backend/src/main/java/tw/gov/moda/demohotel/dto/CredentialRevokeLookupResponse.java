package tw.gov.moda.demohotel.dto;

/**
 * 繁體中文註解：回傳撤銷查詢結果，告知是否找到 CID。
 */
public class CredentialRevokeLookupResponse {

    private boolean found;
    private String cid;
    private String message;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
