package tw.gov.moda.demohotel.service;

import org.springframework.stereotype.Service;
import tw.gov.moda.demohotel.client.VerifierClient;
import tw.gov.moda.demohotel.client.dto.VerifierQrcodeResponse;
import tw.gov.moda.demohotel.client.dto.VerifierResultData;
import tw.gov.moda.demohotel.client.dto.VerifierResultField;
import tw.gov.moda.demohotel.client.dto.VerifierResultResponse;
import tw.gov.moda.demohotel.config.WalletApiProperties;
import tw.gov.moda.demohotel.dto.DriverLicenseClaim;
import tw.gov.moda.demohotel.dto.DriverLicenseVerificationInitResponse;
import tw.gov.moda.demohotel.dto.DriverLicenseVerificationResultResponse;
import tw.gov.moda.demohotel.exception.ExternalApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 繁體中文註解：封裝駕照 VP 的驗證流程。
 */
@Service
public class DriverLicenseVerificationService {

    private static final int DEFAULT_EXPIRE_SECONDS = 300;
    private static final List<String> PREFERRED_ORDER = Arrays.asList(
            "license_type",
            "name",
            "license_number",
            "gender",
            "roc_birthday",
            "registered_address",
            "expired_date",
            "issue_date"
    );

    private final VerifierClient verifierClient;
    private final WalletApiProperties walletApiProperties;

    public DriverLicenseVerificationService(VerifierClient verifierClient, WalletApiProperties walletApiProperties) {
        this.verifierClient = verifierClient;
        this.walletApiProperties = walletApiProperties;
    }

    /**
     * 繁體中文註解：產生駕照驗證 QR Code。
     *
     * @return DriverLicenseVerificationInitResponse
     */
    public DriverLicenseVerificationInitResponse createVerification() {
        WalletApiProperties.DriverLicenseVp config = walletApiProperties.getDriverLicenseVp();
        if (config.getRef() == null || config.getRef().trim().length() == 0) {
            throw new ExternalApiException("駕照驗證 ref 尚未設定");
        }

        String requestTransactionId = UUID.randomUUID().toString();
        VerifierQrcodeResponse response = verifierClient.createAuthorizationQrcode(config.getRef(), requestTransactionId, "N");
        if (response == null) {
            throw new ExternalApiException("DWVP-101 未回傳資料");
        }

        String actualTransactionId = response.getTransactionId() != null && response.getTransactionId().trim().length() > 0
                ? response.getTransactionId()
                : requestTransactionId;

        DriverLicenseVerificationInitResponse result = new DriverLicenseVerificationInitResponse();
        result.setTransactionId(actualTransactionId);
        result.setQrcodeImage(response.getQrcodeImage());
        result.setAuthUri(response.getAuthUri());
        result.setGeneratedAt(LocalDateTime.now());
        result.setExpiresIn(DEFAULT_EXPIRE_SECONDS);
        return result;
    }

    /**
     * 繁體中文註解：查詢駕照驗證結果。
     *
     * @param transactionId 交易序號
     * @return DriverLicenseVerificationResultResponse
     */
    public DriverLicenseVerificationResultResponse queryVerification(String transactionId) {
        VerifierResultResponse response = verifierClient.queryVerificationResult(transactionId);

        DriverLicenseVerificationResultResponse result = new DriverLicenseVerificationResultResponse();
        result.setTransactionId(transactionId);
        result.setLastUpdated(LocalDateTime.now());

        if (response == null || response.getVerifyResult() == null) {
            result.setStatus("WAITING");
            result.setMessage("尚未完成掃描，請請旅客打開數位憑證皮夾掃描 QR Code");
            result.setVerified(false);
            return result;
        }

        boolean verified = Boolean.TRUE.equals(response.getVerifyResult());
        result.setVerified(verified);
        result.setStatus(verified ? "VERIFIED" : "FAILED");
        result.setMessage(response.getResultDescription() != null ? response.getResultDescription() : response.getMessage());

        List<DriverLicenseClaim> claims = extractClaims(response);
        result.setClaims(claims);
        return result;
    }

    private List<DriverLicenseClaim> extractClaims(VerifierResultResponse response) {
        Map<String, VerifierResultField> fieldMap = new LinkedHashMap<String, VerifierResultField>();

        if (response.getData() != null) {
            for (VerifierResultData data : response.getData()) {
                if (data.getClaims() == null) {
                    continue;
                }
                for (VerifierResultField field : data.getClaims()) {
                    if (field != null && field.getEname() != null) {
                        fieldMap.put(field.getEname(), field);
                    }
                }
            }
        }

        List<DriverLicenseClaim> claims = new ArrayList<DriverLicenseClaim>();

        for (String key : PREFERRED_ORDER) {
            if (fieldMap.containsKey(key)) {
                VerifierResultField field = fieldMap.remove(key);
                claims.add(toClaim(field));
            }
        }

        for (VerifierResultField field : fieldMap.values()) {
            claims.add(toClaim(field));
        }

        return claims;
    }

    private DriverLicenseClaim toClaim(VerifierResultField field) {
        DriverLicenseClaim claim = new DriverLicenseClaim();
        claim.setEname(field.getEname());
        claim.setLabel(field.getCname() != null && field.getCname().trim().length() > 0 ? field.getCname() : field.getEname());
        claim.setValue(field.getValue());
        return claim;
    }
}
