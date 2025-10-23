package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVC-202 的回應資料。
 */
public class IssuerVcByDataTagResponse {

    /**
     * 繁體中文註解：查詢使用的資料標籤。
     */
    private String dataTag;

    /**
     * 繁體中文註解：符合條件的 VC 列表。
     */
    private List<VcBasicInfo> vcList = new ArrayList<VcBasicInfo>();

    public String getDataTag() {
        return dataTag;
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public List<VcBasicInfo> getVcList() {
        return vcList;
    }

    public void setVcList(List<VcBasicInfo> vcList) {
        if (vcList == null) {
            this.vcList = new ArrayList<VcBasicInfo>();
        } else {
            this.vcList = vcList;
        }
    }
}
