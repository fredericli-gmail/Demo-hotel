package tw.gov.moda.demohotel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.gov.moda.demohotel.client.IssuerClient;
import tw.gov.moda.demohotel.client.dto.IssuerCredentialStatusChangeResponse;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeRequest;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeResponse;
import tw.gov.moda.demohotel.domain.VcCredential;
import tw.gov.moda.demohotel.repository.VcCredentialRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 繁體中文註解：處理 VC 狀態變更與本地端同步。
 */
@Service
public class CredentialLifecycleService {

    private final IssuerClient issuerClient;
    private final VcCredentialRepository vcCredentialRepository;

    public CredentialLifecycleService(IssuerClient issuerClient, VcCredentialRepository vcCredentialRepository) {
        this.issuerClient = issuerClient;
        this.vcCredentialRepository = vcCredentialRepository;
    }

    /**
     * 繁體中文註解：呼叫 DWVC-301 變更單張卡片狀態。
     *
     * @param cid    卡片識別碼
     * @param action 操作類型
     * @return IssuerCredentialStatusChangeResponse
     */
    @Transactional
    public IssuerCredentialStatusChangeResponse changeStatus(String cid, String action) {
        return changeStatus(cid, action, null);
    }

    public IssuerCredentialStatusChangeResponse changeStatus(String cid, String action, String vcUid) {
        IssuerCredentialStatusChangeResponse response = issuerClient.changeCredentialStatus(cid, action);
        syncCredentialStatus(cid, response.getCredentialStatus(), vcUid);
        return response;
    }

    /**
     * 繁體中文註解：呼叫 DWVC-302 批次變更狀態。
     *
     * @param request 批次請求
     * @return IssuerMultiStatusChangeResponse
     */
    @Transactional
    public IssuerMultiStatusChangeResponse changeMultipleStatus(IssuerMultiStatusChangeRequest request) {
        IssuerMultiStatusChangeResponse response = issuerClient.changeMultipleCredentials(request);
        if (response != null && response.getSuccess() != null) {
            for (String cid : response.getSuccess()) {
                syncCredentialStatus(cid, response.getAction());
            }
        }
        return response;
    }

    /**
     * 繁體中文註解：將最新狀態更新至資料庫。
     *
     * @param cid    卡片識別碼
     * @param status 最新狀態
     */
    private void syncCredentialStatus(String cid, String status, String vcUid) {
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
        credential.setCredentialStatus(status);
        if (vcUid != null && vcUid.trim().length() > 0) {
            credential.setVcUid(vcUid);
        }
        credential.setUpdatedAt(LocalDateTime.now());
        vcCredentialRepository.save(credential);
    }
}
