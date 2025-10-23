package tw.gov.moda.demohotel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.gov.moda.demohotel.client.IssuerClient;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaRequest;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcByDataTagResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcQueryResponse;
import tw.gov.moda.demohotel.domain.VcCredential;
import tw.gov.moda.demohotel.repository.VcCredentialRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 繁體中文註解：管理額外的查詢邏輯，並將 CID 等資訊同步至本地資料庫。
 */
@Service
public class IssueStatusService {

    private final IssuerClient issuerClient;
    private final VcCredentialRepository vcCredentialRepository;

    public IssueStatusService(IssuerClient issuerClient, VcCredentialRepository vcCredentialRepository) {
        this.issuerClient = issuerClient;
        this.vcCredentialRepository = vcCredentialRepository;
    }

    /**
     * 繁體中文註解：透過交易序號向發行端查詢憑證內容。
     *
     * @param transactionId 交易序號
     * @return IssuerVcQueryResponse
     */
    public IssuerVcQueryResponse queryByTransaction(String transactionId) {
        IssuerVcQueryResponse response = issuerClient.queryByTransactionId(transactionId);
        updateCredentialCache(response);
        return response;
    }

    /**
     * 繁體中文註解：透過 dataTag 查詢憑證清單，並回傳原始結果。
     *
     * @param dataTag 資料標籤
     * @param page    頁碼
     * @param size    每頁筆數
     * @return IssuerVcByDataTagResponse
     */
    public IssuerVcByDataTagResponse queryByDataTag(String dataTag, Integer page, Integer size) {
        return issuerClient.queryByDataTag(dataTag, page, size);
    }

    /**
     * 繁體中文註解：透過複合條件查詢憑證。
     *
     * @param request 查詢條件
     * @return IssuerVcByCriteriaResponse
     */
    public IssuerVcByCriteriaResponse queryByCriteria(IssuerVcByCriteriaRequest request) {
        return issuerClient.queryByCriteria(request);
    }

    /**
     * 繁體中文註解：解析 JWT 取得 CID，並同步至資料庫。此處僅保留示意流程，實際解析需完成 JWT 驗簽。
     *
     * @param response 發行端回應
     */
    @Transactional
    protected void updateCredentialCache(IssuerVcQueryResponse response) {
        if (response == null) {
            return;
        }
        String credentialJwt = response.getCredential();
        if (credentialJwt == null || credentialJwt.trim().length() == 0) {
            return;
        }
        String cid = extractCidFromJwt(credentialJwt);
        if (cid == null || cid.trim().length() == 0) {
            return;
        }
        Optional<VcCredential> optionalCredential = vcCredentialRepository.findByCid(cid);
        VcCredential credential;
        if (optionalCredential.isPresent()) {
            credential = optionalCredential.get();
        } else {
            credential = new VcCredential();
            credential.setCid(cid);
            credential.setCreatedAt(LocalDateTime.now());
        }
        credential.setUpdatedAt(LocalDateTime.now());
        vcCredentialRepository.save(credential);
    }

    /**
     * 繁體中文註解：示意用的 CID 解析邏輯，實務上需改為解析 JWT payload。
     *
     * @param jwt JWT 字串
     * @return CID 字串
     */
    private String extractCidFromJwt(String jwt) {
        if (jwt == null || jwt.trim().length() == 0) {
            return null;
        }
        int credentialIndex = jwt.indexOf("credential/");
        if (credentialIndex < 0) {
            return null;
        }
        String substring = jwt.substring(credentialIndex + "credential/".length());
        int endIndex = substring.indexOf("\"");
        if (endIndex < 0) {
            return substring;
        }
        return substring.substring(0, endIndex);
    }
}
