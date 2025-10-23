package tw.gov.moda.demohotel.service;

import org.springframework.stereotype.Service;
import tw.gov.moda.demohotel.client.VerifierClient;
import tw.gov.moda.demohotel.client.dto.VerifierDeepLinkResponse;
import tw.gov.moda.demohotel.client.dto.VerifierQrcodeResponse;
import tw.gov.moda.demohotel.client.dto.VerifierResultRequest;
import tw.gov.moda.demohotel.client.dto.VerifierResultResponse;

/**
 * 繁體中文註解：整合驗證端模組相關操作。
 */
@Service
public class VerifierService {

    private final VerifierClient verifierClient;

    public VerifierService(VerifierClient verifierClient) {
        this.verifierClient = verifierClient;
    }

    public VerifierQrcodeResponse createVerificationQrcode(String ref, String transactionId, boolean enableCallback) {
        String isCallback = enableCallback ? "Y" : "N";
        return verifierClient.createAuthorizationQrcode(ref, transactionId, isCallback);
    }

    public VerifierResultResponse queryVerificationResult(String transactionId) {
        VerifierResultRequest request = new VerifierResultRequest();
        request.setTransactionId(transactionId);
        return verifierClient.queryVerificationResult(request);
    }

    public VerifierDeepLinkResponse requestDeepLink(String vpUid) {
        return verifierClient.requestDeepLink(vpUid);
    }
}
