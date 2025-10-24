package tw.gov.moda.demohotel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.gov.moda.demohotel.client.IssuerClient;
import tw.gov.moda.demohotel.client.dto.IssuerIssueRequest;
import tw.gov.moda.demohotel.client.dto.IssuerIssueResponse;
import tw.gov.moda.demohotel.client.dto.VcField;
import tw.gov.moda.demohotel.domain.VcIssueRecord;
import tw.gov.moda.demohotel.model.BreakfastIssueCommand;
import tw.gov.moda.demohotel.model.IssueCommand;
import tw.gov.moda.demohotel.repository.VcIssueRecordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：負責整合發卡流程，包含資料轉換、呼叫外部 API 以及紀錄交易。
 */
@Service
public class IssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        IssuerIssueRequest request = buildRoomRequest(command);
        LOGGER.info("開始呼叫 DWVC-101，vcUid={}, dataTag={}, roomNb={}", command.getVcUid(), command.getDataTag(), command.getRoomNb());
        LOGGER.info("DWVC-101 請求內容：{}", toJsonSafe(request));
        IssuerIssueResponse response = issuerClient.issueCredential(request);
        persistIssueRecord(command.getVcUid(), command.getDataTag(), command.getCheckInDate(), command.getCheckOutDate(), response.getTransactionId());
        LOGGER.info("DWVC-101 回傳交易序號：{}", response.getTransactionId());
        return response;
    }

    /**
     * 繁體中文註解：發行早餐券。
     *
     * @param command 早餐券資料
     * @return IssuerIssueResponse
     */
    @Transactional
    public IssuerIssueResponse issueBreakfastTicket(BreakfastIssueCommand command) {
        IssuerIssueRequest request = buildBreakfastRequest(command);
        LOGGER.info("開始呼叫 DWVC-101，vcUid={}, dataTag={}, roomNb={}", command.getVcUid(), command.getDataTag(), command.getRoomNb());
        LOGGER.info("DWVC-101 請求內容：{}", toJsonSafe(request));
        IssuerIssueResponse response = issuerClient.issueCredential(request);
        persistIssueRecord(command.getVcUid(), command.getDataTag(), command.getValidDate(), command.getValidDate(), response.getTransactionId());
        LOGGER.info("DWVC-101 回傳交易序號：{}", response.getTransactionId());
        return response;
    }

    /**
     * 繁體中文註解：依據輸入資料建立 DWVC-101 請求物件。
     *
     * @param command 房卡資訊
     * @return IssuerIssueRequest
     */
    private IssuerIssueRequest buildRoomRequest(IssueCommand command) {
        IssuerIssueRequest request = new IssuerIssueRequest();
        request.setVcUid(command.getVcUid());
        request.setIssuanceDate(command.getCheckInDate());
        request.setExpiredDate(command.getCheckOutDate());
        if (command.getDataTag() != null && !command.getDataTag().isBlank()) {
            request.setDataTag(command.getDataTag());
        }

        List<VcField> fields = new ArrayList<VcField>();

        VcField roomNumberField = new VcField();
        roomNumberField.setEname("room_nb");
        roomNumberField.setContent(command.getRoomNb());
        fields.add(roomNumberField);

        if (command.getRoomType() != null && !command.getRoomType().isBlank()) {
            VcField roomTypeField = new VcField();
            roomTypeField.setEname("room_type");
            roomTypeField.setContent(command.getRoomType());
            fields.add(roomTypeField);
        }

        if (command.getRoomMemo() != null && !command.getRoomMemo().isBlank()) {
            VcField roomMemoField = new VcField();
            roomMemoField.setEname("room_memo");
            roomMemoField.setContent(command.getRoomMemo());
            fields.add(roomMemoField);
        }

        request.setFields(fields);

        return request;
    }

    /**
     * 繁體中文註解：組裝早餐券的 DWVC-101 請求內容。
     *
     * @param command 早餐券資料
     * @return IssuerIssueRequest
     */
    private IssuerIssueRequest buildBreakfastRequest(BreakfastIssueCommand command) {
        IssuerIssueRequest request = new IssuerIssueRequest();
        request.setVcUid(command.getVcUid());
        LocalDate validDate = LocalDate.parse(command.getValidDate(), DateTimeFormatter.BASIC_ISO_DATE);
        request.setIssuanceDate(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        request.setExpiredDate(validDate.plusMonths(5).format(DateTimeFormatter.BASIC_ISO_DATE));
        if (command.getDataTag() != null && !command.getDataTag().isBlank()) {
            request.setDataTag(command.getDataTag());
        }

        List<VcField> fields = new ArrayList<VcField>();

        VcField roomNumberField = new VcField();
        roomNumberField.setEname("room_nb");
        roomNumberField.setContent(command.getRoomNb());
        fields.add(roomNumberField);

        if (command.getTicketType() != null && !command.getTicketType().isBlank()) {
            VcField ticketTypeField = new VcField();
            ticketTypeField.setEname("ticket_type");
            ticketTypeField.setContent(command.getTicketType());
            fields.add(ticketTypeField);
        }

        if (command.getLocation() != null && !command.getLocation().isBlank()) {
            VcField locationField = new VcField();
            locationField.setEname("location");
            locationField.setContent(command.getLocation());
            fields.add(locationField);
        }

        request.setFields(fields);
        return request;
    }

    /**
     * 繁體中文註解：將發卡交易存入資料庫。
     *
     * @param command  原始輸入資料
     * @param response 外部 API 回應
     */
    private void persistIssueRecord(String vcUid, String dataTag, String issuanceDate, String expiredDate, String transactionId) {
        VcIssueRecord record = new VcIssueRecord();
        record.setTransactionId(transactionId);
        record.setVcUid(vcUid);
        if (dataTag != null && !dataTag.isBlank()) {
            record.setDataTag(dataTag);
        } else {
            record.setDataTag(null);
        }
        record.setIssuanceDate(issuanceDate);
        record.setExpiredDate(expiredDate);
        record.setCreatedAt(LocalDateTime.now());
        vcIssueRecordRepository.save(record);
    }

    private String toJsonSafe(IssuerIssueRequest request) {
        try {
            return OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException ex) {
            LOGGER.warn("DWVC-101 請求內容序列化失敗", ex);
            return "序列化失敗：" + ex.getMessage();
        }
    }
}
