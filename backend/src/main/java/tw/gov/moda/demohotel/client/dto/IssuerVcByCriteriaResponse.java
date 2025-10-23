package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVC-203 的回應資料。
 */
public class IssuerVcByCriteriaResponse {

    private List<VcByCriteriaItem> vcList = new ArrayList<VcByCriteriaItem>();

    public List<VcByCriteriaItem> getVcList() {
        return vcList;
    }

    public void setVcList(List<VcByCriteriaItem> vcList) {
        if (vcList == null) {
            this.vcList = new ArrayList<VcByCriteriaItem>();
        } else {
            this.vcList = vcList;
        }
    }

    /**
     * 繁體中文註解：封裝條件查詢結果的單筆資料。
     */
    public static class VcByCriteriaItem {
        private String dataTag;
        private String cid;
        private String vcUid;
        private String issuranceDate;
        private String expirationDate;
        private String credentialStatus;

        public String getDataTag() {
            return dataTag;
        }

        public void setDataTag(String dataTag) {
            this.dataTag = dataTag;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getVcUid() {
            return vcUid;
        }

        public void setVcUid(String vcUid) {
            this.vcUid = vcUid;
        }

        public String getIssuranceDate() {
            return issuranceDate;
        }

        public void setIssuranceDate(String issuranceDate) {
            this.issuranceDate = issuranceDate;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCredentialStatus() {
            return credentialStatus;
        }

        public void setCredentialStatus(String credentialStatus) {
            this.credentialStatus = credentialStatus;
        }
    }
}
