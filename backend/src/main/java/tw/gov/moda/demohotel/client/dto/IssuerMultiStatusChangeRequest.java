package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVC-302 的請求資料。
 */
public class IssuerMultiStatusChangeRequest {

    private String action;
    private List<String> cids = new ArrayList<String>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
