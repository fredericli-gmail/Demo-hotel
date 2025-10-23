package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：對應 DWVP-01-201 的回應資料。
 */
public class VerifierResultResponse {

    private String code;
    private String message;
    private Boolean verifyResult;
    private String resultDescription;
    private String transactionId;
    private List<VerifierResultData> data = new ArrayList<VerifierResultData>();

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

    public Boolean getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(Boolean verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<VerifierResultData> getData() {
        return data;
    }

    public void setData(List<VerifierResultData> data) {
        if (data == null) {
            this.data = new ArrayList<VerifierResultData>();
        } else {
            this.data = data;
        }
    }
}
