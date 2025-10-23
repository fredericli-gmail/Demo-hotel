package tw.gov.moda.demohotel.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tw.gov.moda.demohotel.client.dto.VerifierDeepLinkResponse;
import tw.gov.moda.demohotel.client.dto.VerifierQrcodeResponse;
import tw.gov.moda.demohotel.client.dto.VerifierResultRequest;
import tw.gov.moda.demohotel.client.dto.VerifierResultResponse;
import tw.gov.moda.demohotel.config.WalletApiProperties;
import tw.gov.moda.demohotel.exception.ExternalApiException;

/**
 * 繁體中文註解：封裝對驗證端模組的 HTTP 呼叫，涵蓋 DWVP 系列 API。
 */
@Component
public class VerifierClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifierClient.class);

    private final RestTemplate restTemplate;
    private final WalletApiProperties walletApiProperties;

    public VerifierClient(RestTemplate restTemplate, WalletApiProperties walletApiProperties) {
        this.restTemplate = restTemplate;
        this.walletApiProperties = walletApiProperties;
    }

    /**
     * 繁體中文註解：呼叫 DWVP-01-101，取得授權要求的 QR Code。
     *
     * @param ref            VP 範本代碼
     * @param transactionId  本地產生的交易序號
     * @param isCallback     是否啟用 callback
     * @return VerifierQrcodeResponse
     */
    public VerifierQrcodeResponse createAuthorizationQrcode(String ref, String transactionId, String isCallback) {
        String baseUrl = walletApiProperties.getVerifier().getBaseUrl() + "/api/oidvp/qrcode";
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("ref", ref);
        params.add("transactionId", transactionId);
        if (isCallback != null && isCallback.trim().length() > 0) {
            params.add("isCallBack", isCallback);
        }
        HttpHeaders headers = createJsonHeaders(walletApiProperties.getVerifier().getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<Void>(headers);
        try {
            ResponseEntity<VerifierQrcodeResponse> response = restTemplate.exchange(
                    baseUrl + "?ref={ref}&transactionId={transactionId}" + (isCallback != null && isCallback.trim().length() > 0 ? "&isCallBack={isCallBack}" : ""),
                    HttpMethod.GET,
                    entity,
                    VerifierQrcodeResponse.class,
                    params);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVP-01-101 失敗", ex);
            throw new ExternalApiException("DWVP-01-101 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVP-01-201，查詢驗證結果。
     *
     * @param request 查詢請求
     * @return VerifierResultResponse
     */
    public VerifierResultResponse queryVerificationResult(VerifierResultRequest request) {
        String url = walletApiProperties.getVerifier().getBaseUrl() + "/api/oidvp/result";
        HttpEntity<VerifierResultRequest> entity = new HttpEntity<VerifierResultRequest>(request, createJsonHeaders(walletApiProperties.getVerifier().getAccessToken()));
        try {
            ResponseEntity<VerifierResultResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    VerifierResultResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVP-01-201 失敗", ex);
            throw new ExternalApiException("DWVP-01-201 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVP-02-101，透過 vpUid 取得 deep link。
     *
     * @param vpUid VP 模板代碼
     * @return VerifierDeepLinkResponse
     */
    public VerifierDeepLinkResponse requestDeepLink(String vpUid) {
        String url = walletApiProperties.getVerifier().getBaseUrl() + "/" + vpUid;
        HttpEntity<Void> entity = new HttpEntity<Void>(createJsonHeaders(walletApiProperties.getVerifier().getAccessToken()));
        try {
            ResponseEntity<VerifierDeepLinkResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    VerifierDeepLinkResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVP-02-101 失敗", ex);
            throw new ExternalApiException("DWVP-02-101 呼叫失敗", ex);
        }
    }

    private HttpHeaders createJsonHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (accessToken != null && accessToken.trim().length() > 0) {
            headers.set("Access-Token", accessToken);
        }
        return headers;
    }
}
