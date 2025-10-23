package tw.gov.moda.demohotel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.gov.moda.demohotel.client.IssuerClient;
import tw.gov.moda.demohotel.client.dto.IssuerIssueRequest;
import tw.gov.moda.demohotel.client.dto.IssuerIssueResponse;
import tw.gov.moda.demohotel.client.dto.VcField;
import tw.gov.moda.demohotel.domain.VcIssueRecord;
import tw.gov.moda.demohotel.model.IssueCommand;
import tw.gov.moda.demohotel.repository.VcIssueRecordRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：負責整合發卡流程，包含資料轉換、呼叫外部 API 以及紀錄交易。
 */
@Service
public class IssueService {

    private final IssuerClient issuerClient;
    private final VcIssueRecordRepository vcIssueRecordRepository;

    public IssueService(IssuerClient issuerClient, VcIssueRecordRepository vcIssueRecordRepository) {
        this.issuerClient = issuerClient;
        this.vcIssueRecordRepository = vcIssueRecordRepository;
    }

    /**
     * 繁體中文註解：進行房卡發行，並回傳發卡結果。
     *
     * @param command 前端送入的房卡資料
     * @return IssuerIssueResponse API 回應
     */
    @Transactional
    public IssuerIssueResponse issueRoomCredential(IssueCommand command) {
        IssuerIssueRequest request = buildIssuerRequest(command);
        IssuerIssueResponse response = issuerClient.issueCredential(request);
        persistIssueRecord(command, response);
        return response;
    }

    /**
     * 繁體中文註解：依據輸入資料建立 DWVC-101 請求物件。
     *
     * @param command 房卡資訊
     * @return IssuerIssueRequest
     */
    private IssuerIssueRequest buildIssuerRequest(IssueCommand command) {
        IssuerIssueRequest request = new IssuerIssueRequest();
        request.setVcUid(command.getVcUid());
        request.setIssuanceDate(command.getCheckInDate());
        request.setExpiredDate(command.getCheckOutDate());
        request.setDataTag(command.getDataTag());

        List<VcField> fields = new ArrayList<VcField>();

        VcField nameField = new VcField();
        nameField.setEname("name");
        nameField.setContent(command.getGuestName());
        fields.add(nameField);

        VcField roomField = new VcField();
        roomField.setEname("room_number");
        roomField.setContent(command.getRoomNumber());
        fields.add(roomField);

        request.setFields(fields);

        return request;
    }

    /**
     * 繁體中文註解：將發卡交易存入資料庫。
     *
     * @param command  原始輸入資料
     * @param response 外部 API 回應
     */
    private void persistIssueRecord(IssueCommand command, IssuerIssueResponse response) {
        VcIssueRecord record = new VcIssueRecord();
        record.setTransactionId(response.getTransactionId());
        record.setVcUid(command.getVcUid());
        record.setDataTag(command.getDataTag());
        record.setIssuanceDate(command.getCheckInDate());
        record.setExpiredDate(command.getCheckOutDate());
        record.setCreatedAt(LocalDateTime.now());
        vcIssueRecordRepository.save(record);
    }
}
