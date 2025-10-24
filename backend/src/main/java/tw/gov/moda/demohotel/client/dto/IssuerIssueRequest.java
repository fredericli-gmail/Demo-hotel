package tw.gov.moda.demohotel.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVC-101 的請求物件，封裝發卡所需資料。
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IssuerIssueRequest {

    /**
     * 繁體中文註解：VC 模板代碼。
     */
    private String vcUid;

    /**
     * 繁體中文註解：卡片發行日，格式 YYYYMMDD。
     */
    private String issuanceDate;

    /**
     * 繁體中文註解：卡片到期日，格式 YYYYMMDD。
     */
    private String expiredDate;

    /**
     * 繁體中文註解：自定義資料標籤，方便後續查詢。
     */
    private String dataTag;

    /**
     * 繁體中文註解：卡片欄位內容列表。
     */
    private List<VcField> fields = new ArrayList<VcField>();

    /**
     * 繁體中文註解：欲廢止的既有卡片識別碼集合。
     */
    private List<String> cids = new ArrayList<String>();

    public String getVcUid() {
        return vcUid;
    }

    public void setVcUid(String vcUid) {
        this.vcUid = vcUid;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public List<VcField> getFields() {
        return fields;
    }

    public void setFields(List<VcField> fields) {
        if (fields == null) {
            this.fields = new ArrayList<VcField>();
        } else {
            this.fields = fields;
        }
    }

    public List<String> getCids() {
        return cids;
    }

    public void setCids(List<String> cids) {
        if (cids == null) {
            this.cids = new ArrayList<String>();
        } else {
            this.cids = cids;
        }
    }
}
