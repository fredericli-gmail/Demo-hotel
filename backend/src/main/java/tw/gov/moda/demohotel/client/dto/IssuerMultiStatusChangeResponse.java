package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVC-302 的回應資料。
 */
public class IssuerMultiStatusChangeResponse {

    private String action;
    private List<String> success = new ArrayList<String>();
    private List<IssuerMultiStatusFailure> fail = new ArrayList<IssuerMultiStatusFailure>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<String> getSuccess() {
        return success;
    }

    public void setSuccess(List<String> success) {
        if (success == null) {
            this.success = new ArrayList<String>();
        } else {
            this.success = success;
        }
    }

    public List<IssuerMultiStatusFailure> getFail() {
        return fail;
    }

    public void setFail(List<IssuerMultiStatusFailure> fail) {
        if (fail == null) {
            this.fail = new ArrayList<IssuerMultiStatusFailure>();
        } else {
            this.fail = fail;
        }
    }
}
