package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：紀錄批次狀態變更中失敗的項目與原因。
 */
public class IssuerMultiStatusFailure {

    private String code;
    private String message;
    private List<String> cids = new ArrayList<String>();

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
