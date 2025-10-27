package tw.gov.moda.demohotel.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tw.gov.moda.demohotel.client.dto.IssuerCredentialStatusChangeResponse;
import tw.gov.moda.demohotel.client.dto.IssuerIssueRequest;
import tw.gov.moda.demohotel.client.dto.IssuerIssueResponse;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeRequest;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaRequest;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcByDataTagResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcQueryResponse;
import tw.gov.moda.demohotel.config.WalletApiProperties;
import tw.gov.moda.demohotel.exception.CredentialPendingException;
import tw.gov.moda.demohotel.exception.ExternalApiException;

import java.util.Collections;
import java.util.Map;

/**
 * 繁體中文註解：封裝對發行端模組的 HTTP 呼叫，涵蓋 DWVC 系列 API。
 */
@Component
public class IssuerClient {

    /**
     * 繁體中文註解：紀錄外部呼叫資訊，以利問題追蹤。
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssuerClient.class);

    private final RestTemplate restTemplate;
    private final WalletApiProperties walletApiProperties;

    /**
     * 繁體中文註解：建構子注入 RestTemplate 與設定檔。
     *
     * @param restTemplate         共用 HTTP 客戶端
     * @param walletApiProperties  存放發行端設定
     */
    public IssuerClient(RestTemplate restTemplate, WalletApiProperties walletApiProperties) {
        this.restTemplate = restTemplate;
        this.walletApiProperties = walletApiProperties;
    }

    /**
     * 繁體中文註解：呼叫 DWVC-101，要求產生 QR Code 與交易序號。
     *
     * @param request 請求內容
     * @return IssuerIssueResponse 回應資料
     */
    public IssuerIssueResponse issueCredential(IssuerIssueRequest request) {
        String url = walletApiProperties.getIssuer().getBaseUrl() + "/api/qrcode/data";
        HttpEntity<IssuerIssueRequest> entity = new HttpEntity<IssuerIssueRequest>(request, createJsonHeaders(walletApiProperties.getIssuer().getAccessToken()));
        try {
            ResponseEntity<IssuerIssueResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    IssuerIssueResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            LOGGER.error("呼叫 DWVC-101 失敗，狀態碼={}，回應內容={}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            String message = "DWVC-101 呼叫失敗（狀態：" + ex.getStatusCode() + "，回應：" + sanitizeResponse(ex.getResponseBodyAsString()) + "）";
            throw new ExternalApiException(message, ex);
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-101 失敗，非預期錯誤", ex);
            throw new ExternalApiException("DWVC-101 呼叫失敗（非預期錯誤）", ex);
        }
    }

    private String sanitizeResponse(String body) {
        if (body == null || body.isBlank()) {
            return "無內容";
        }
        return body.replaceAll("\\s+", " ").trim();
    }

    /**
     * 繁體中文註解：呼叫 DWVC-201，以交易序號取得 JWT 憑證內容。
     *
     * @param transactionId 交易序號
     * @return IssuerVcQueryResponse 回應資料
     */
    public IssuerVcQueryResponse queryByTransactionId(String transactionId) {
        String url = walletApiProperties.getIssuer().getBaseUrl() + "/api/credential/nonce/" + transactionId;
        HttpEntity<Void> entity = new HttpEntity<Void>(createJsonHeaders(walletApiProperties.getIssuer().getAccessToken()));
        try {
            ResponseEntity<IssuerVcQueryResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    IssuerVcQueryResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == 400) {
                throw new CredentialPendingException("Credential not ready yet", ex);
            }
            LOGGER.error("呼叫 DWVC-201 失敗，transactionId={}，狀態={} 內容={}",
                    transactionId, ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            throw new ExternalApiException("DWVC-201 呼叫失敗", ex);
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-201 失敗，transactionId={}", transactionId, ex);
            throw new ExternalApiException("DWVC-201 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVC-202，透過 dataTag 查詢憑證列表。
     *
     * @param dataTag 標籤
     * @param page    頁碼
     * @param size    每頁筆數
     * @return IssuerVcByDataTagResponse 回應資料
     */
    public IssuerVcByDataTagResponse queryByDataTag(String dataTag, Integer page, Integer size) {
        StringBuilder urlBuilder = new StringBuilder(walletApiProperties.getIssuer().getBaseUrl())
                .append("/api/credential/datatag/")
                .append(dataTag);
        if (page != null || size != null) {
            urlBuilder.append("?");
            boolean appended = false;
            if (page != null) {
                urlBuilder.append("page=").append(page);
                appended = true;
            }
            if (size != null) {
                if (appended) {
                    urlBuilder.append("&");
                }
                urlBuilder.append("size=").append(size);
            }
        }
        String url = urlBuilder.toString();
        HttpHeaders headers = createJsonHeaders(walletApiProperties.getIssuer().getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<Void>(headers);
        try {
            ResponseEntity<IssuerVcByDataTagResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    IssuerVcByDataTagResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-202 失敗，dataTag={}", dataTag, ex);
            throw new ExternalApiException("DWVC-202 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVC-203，透過條件查詢憑證。
     *
     * @param request 查詢條件
     * @return IssuerVcByCriteriaResponse 回應資料
     */
    public IssuerVcByCriteriaResponse queryByCriteria(IssuerVcByCriteriaRequest request) {
        String url = walletApiProperties.getIssuer().getBaseUrl() + "/api/credential/vcdata";
        HttpEntity<IssuerVcByCriteriaRequest> entity = new HttpEntity<IssuerVcByCriteriaRequest>(request, createJsonHeaders(walletApiProperties.getIssuer().getAccessToken()));
        try {
            ResponseEntity<IssuerVcByCriteriaResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    IssuerVcByCriteriaResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-203 失敗", ex);
            throw new ExternalApiException("DWVC-203 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVC-301，變更單筆憑證狀態。
     *
     * @param cid      憑證識別碼
     * @param action   操作類型
     * @return IssuerCredentialStatusChangeResponse 回應資料
     */
    public IssuerCredentialStatusChangeResponse changeCredentialStatus(String cid, String action) {
        String url = walletApiProperties.getIssuer().getBaseUrl() + "/api/credential/" + cid + "/" + action;
        HttpEntity<Void> entity = new HttpEntity<Void>(createJsonHeaders(walletApiProperties.getIssuer().getAccessToken()));
        try {
            ResponseEntity<IssuerCredentialStatusChangeResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    IssuerCredentialStatusChangeResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-301 失敗，cid={}", cid, ex);
            throw new ExternalApiException("DWVC-301 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：呼叫 DWVC-302，批次變更憑證狀態。
     *
     * @param request 批次操作內容
     * @return IssuerMultiStatusChangeResponse 回應資料
     */
    public IssuerMultiStatusChangeResponse changeMultipleCredentials(IssuerMultiStatusChangeRequest request) {
        String url = walletApiProperties.getIssuer().getBaseUrl() + "/api/credential/multiaction";
        HttpEntity<IssuerMultiStatusChangeRequest> entity = new HttpEntity<IssuerMultiStatusChangeRequest>(request, createJsonHeaders(walletApiProperties.getIssuer().getAccessToken()));
        try {
            ResponseEntity<IssuerMultiStatusChangeResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    IssuerMultiStatusChangeResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            LOGGER.error("呼叫 DWVC-302 失敗", ex);
            throw new ExternalApiException("DWVC-302 呼叫失敗", ex);
        }
    }

    /**
     * 繁體中文註解：建立共用的 JSON Headers。
     *
     * @param accessToken 官方核發的 Token
     * @return HttpHeaders 物件
     */
    private HttpHeaders createJsonHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (accessToken != null && accessToken.trim().length() > 0) {
            headers.set("Access-Token", accessToken);
        }
        return headers;
    }
}
