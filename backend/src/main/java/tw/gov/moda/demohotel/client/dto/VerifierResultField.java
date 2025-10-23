package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：封裝驗證結果中的欄位資料。
 */
public class VerifierResultField {

    private String ename;
    private String cname;
    private String value;

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
