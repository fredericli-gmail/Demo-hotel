package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：代表 VC 欄位的名稱與內容，對應 fields 陣列。
 */
public class VcField {

    /**
     * 繁體中文註解：欄位英文代碼。
     */
    private String ename;

    /**
     * 繁體中文註解：欄位實際內容。
     */
    private String content;

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
