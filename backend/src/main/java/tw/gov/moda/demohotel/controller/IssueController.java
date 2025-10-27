package tw.gov.moda.demohotel.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tw.gov.moda.demohotel.client.dto.IssuerCredentialStatusChangeResponse;
import tw.gov.moda.demohotel.client.dto.IssuerIssueResponse;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeRequest;
import tw.gov.moda.demohotel.client.dto.IssuerMultiStatusChangeResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaRequest;
import tw.gov.moda.demohotel.client.dto.IssuerVcByCriteriaResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcByDataTagResponse;
import tw.gov.moda.demohotel.client.dto.IssuerVcQueryResponse;
import tw.gov.moda.demohotel.model.BreakfastIssueCommand;
import tw.gov.moda.demohotel.model.IssueCommand;
import tw.gov.moda.demohotel.dto.CredentialCidResponse;
import tw.gov.moda.demohotel.service.CredentialLifecycleService;
import tw.gov.moda.demohotel.service.IssueService;
import tw.gov.moda.demohotel.service.IssueStatusService;

/**
 * 繁體中文註解：提供櫃檯系統呼叫的 REST API。
 */
@RestController
@RequestMapping("/api/credentials")
public class IssueController {

    private final IssueService issueService;
    private final IssueStatusService issueStatusService;
    private final CredentialLifecycleService credentialLifecycleService;

    public IssueController(IssueService issueService,
                           IssueStatusService issueStatusService,
                           CredentialLifecycleService credentialLifecycleService) {
        this.issueService = issueService;
        this.issueStatusService = issueStatusService;
        this.credentialLifecycleService = credentialLifecycleService;
    }

    /**
     * 繁體中文註解：發行電子房卡。
     *
     * @param command 房卡資訊
     * @return 發卡結果
     */
    @PostMapping("/issue")
    public ResponseEntity<IssuerIssueResponse> issue(@Valid @RequestBody IssueCommand command) {
        IssuerIssueResponse response = issueService.issueRoomCredential(command);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：發行早餐券 VC。
     *
     * @param command 早餐券資訊
     * @return 發卡結果
     */
    @PostMapping("/breakfast")
    public ResponseEntity<IssuerIssueResponse> issueBreakfast(@Valid @RequestBody BreakfastIssueCommand command) {
        IssuerIssueResponse response = issueService.issueBreakfastTicket(command);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：透過交易序號查詢憑證。
     *
     * @param transactionId 交易序號
     * @return 憑證內容
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<IssuerVcQueryResponse> queryByTransaction(@PathVariable("transactionId") String transactionId) {
        IssuerVcQueryResponse response = issueStatusService.queryByTransaction(transactionId);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：輪詢憑證 CID，若發卡成功會回傳唯一序號。
     */
    @GetMapping("/transaction/{transactionId}/cid")
    public ResponseEntity<CredentialCidResponse> pollCredentialCid(@PathVariable("transactionId") String transactionId) {
        CredentialCidResponse response = issueService.pollCredentialCid(transactionId);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：透過 dataTag 查詢憑證列表。
     *
     * @param dataTag 標籤
     * @param page    頁碼
     * @param size    筆數
     * @return 查詢結果
     */
    @GetMapping("/datatag/{dataTag}")
    public ResponseEntity<IssuerVcByDataTagResponse> queryByDataTag(@PathVariable("dataTag") String dataTag,
                                                                    @RequestParam(value = "page", required = false) Integer page,
                                                                    @RequestParam(value = "size", required = false) Integer size) {
        IssuerVcByDataTagResponse response = issueStatusService.queryByDataTag(dataTag, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：透過複合條件查詢憑證。
     *
     * @param request 查詢條件
     * @return 查詢結果
     */
    @PostMapping("/search")
    public ResponseEntity<IssuerVcByCriteriaResponse> queryByCriteria(@RequestBody IssuerVcByCriteriaRequest request) {
        IssuerVcByCriteriaResponse response = issueStatusService.queryByCriteria(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：單筆變更卡片狀態。
     *
     * @param cid    卡片識別碼
     * @param action 操作類型
     * @return 回應資訊
     */
    @PutMapping("/{cid}/status/{action}")
    public ResponseEntity<IssuerCredentialStatusChangeResponse> changeStatus(@PathVariable("cid") String cid,
                                                                             @PathVariable("action") String action) {
        IssuerCredentialStatusChangeResponse response = credentialLifecycleService.changeStatus(cid, action);
        return ResponseEntity.ok(response);
    }

    /**
     * 繁體中文註解：批次變更卡片狀態。
     *
     * @param request 批次請求
     * @return 回應資訊
     */
    @PutMapping("/status")
    public ResponseEntity<IssuerMultiStatusChangeResponse> changeStatusBatch(@RequestBody IssuerMultiStatusChangeRequest request) {
        IssuerMultiStatusChangeResponse response = credentialLifecycleService.changeMultipleStatus(request);
        return ResponseEntity.ok(response);
    }
}
