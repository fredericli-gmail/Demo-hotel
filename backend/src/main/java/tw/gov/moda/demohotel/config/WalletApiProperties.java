package tw.gov.moda.demohotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 繁體中文註解：此類別負責讀取發行端與驗證端的 API 設定，方便各服務統一引用。
 */
@Component
@ConfigurationProperties(prefix = "wallet")
public class WalletApiProperties {

    /**
     * 繁體中文註解：發行端模組相關設定物件。
     */
    private final Endpoint issuer = new Endpoint();

    /**
     * 繁體中文註解：驗證端模組相關設定物件。
     */
    private final Endpoint verifier = new Endpoint();

    /**
     * 繁體中文註解：駕照 VP 驗證相關設定。
     */
    private final DriverLicenseVp driverLicenseVp = new DriverLicenseVp();

    /**
     * 繁體中文註解：外部呼叫時使用的設定資料結構。
     */
    public static class Endpoint {

        /**
         * 繁體中文註解：API 基礎路徑。
         */
        private String baseUrl;

        /**
         * 繁體中文註解：官方核發的 Access Token。
         */
        private String accessToken;

        /**
         * 繁體中文註解：提供外部取得 baseUrl。
         *
         * @return baseUrl 字串
         */
        public String getBaseUrl() {
            return baseUrl;
        }

        /**
         * 繁體中文註解：設定 baseUrl。
         *
         * @param baseUrl API 基礎路徑
         */
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        /**
         * 繁體中文註解：提供外部取得 accessToken。
         *
         * @return accessToken 字串
         */
        public String getAccessToken() {
            return accessToken;
        }

        /**
         * 繁體中文註解：設定 accessToken。
         *
         * @param accessToken 外部申請的 Token
         */
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    /**
     * 繁體中文註解：取得發行端設定。
     *
     * @return Endpoint 實例
     */
    public Endpoint getIssuer() {
        return issuer;
    }

    /**
     * 繁體中文註解：取得驗證端設定。
     *
     * @return Endpoint 實例
     */
    public Endpoint getVerifier() {
        return verifier;
    }

    public DriverLicenseVp getDriverLicenseVp() {
        return driverLicenseVp;
    }

    /**
     * 繁體中文註解：駕照 VP 驗證所需參數。
     */
    public static class DriverLicenseVp {

        /**
         * 繁體中文註解：驗證服務代碼 ref。
         */
        private String ref;

        /**
         * 繁體中文註解：VP 模板代碼。
         */
        private String vpUid;

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getVpUid() {
            return vpUid;
        }

        public void setVpUid(String vpUid) {
            this.vpUid = vpUid;
        }
    }
}
