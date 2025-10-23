package tw.gov.moda.demohotel.client.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 繁體中文註解：封裝驗證結果中的單張憑證資料。
 */
public class VerifierResultData {

    private String credentialType;
    private List<VerifierResultField> claims = new ArrayList<VerifierResultField>();

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public List<VerifierResultField> getClaims() {
        return claims;
    }

    public void setClaims(List<VerifierResultField> claims) {
        if (claims == null) {
            this.claims = new ArrayList<VerifierResultField>();
        } else {
            this.claims = claims;
        }
    }
}
