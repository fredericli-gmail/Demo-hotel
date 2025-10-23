package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：對應 DWVC-203 的請求資料。
 */
public class IssuerVcByCriteriaRequest {

    private String dataTag;
    private String vcUid;
    private String credentialStatus;
    private Integer page;
    private Integer size;

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getCredentialStatus() {
        return credentialStatus;
    }

    public void setCredentialStatus(String credentialStatus) {
        this.credentialStatus = credentialStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
