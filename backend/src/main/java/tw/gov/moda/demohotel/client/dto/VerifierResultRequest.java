package tw.gov.moda.demohotel.client.dto;

/**
 * 繁體中文註解：對應 DWVP-01-201 的請求資料。
 */
public class VerifierResultRequest {

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
