# QR Code 解析工具使用說明

## 功能概述
本工具整合於前端選單「QR Code 解析」，可透過後端新增的 `/api/qrcode/decode` 介面進行下列動作：
- **ECC 解密**：採用 X25519 金鑰交換與 ChaCha20-Poly1305 完成解密。
- **HMAC 驗證**：比對解密後明文與 QR Code 內附的 HMAC，確保資料未被竄改。
- **TOTP 驗證**：驗證明文中的一次性密碼，防止舊資料被重複使用。

## 系統設定
請於 `backend/src/main/resources/application.yml` 或環境變數設定以下四個金鑰：

| 參數名稱 | 說明 | 建議來源 |
| --- | --- | --- |
| `security.qr.ecc-public-key` | 提供外部系統加密資料的公鑰 | `QR_ECC_PUBLIC_KEY` |
| `security.qr.ecc-private-key` | 後端解密資料的私鑰 | `QR_ECC_PRIVATE_KEY` |
| `security.qr.hmac-key` | HMAC 驗證金鑰（Base64） | `QR_HMAC_KEY` |
| `security.qr.totp-key` | TOTP 驗證金鑰（Base64） | `QR_TOTP_KEY` |

若以 Docker Compose 開發，可在 `.env` 檔或服務環境變數中覆寫以上設定。

## 操作流程
1. 啟動開發環境：`docker compose up backend frontend db`。
2. 於瀏覽器開啟 `http://localhost:5173`，點選導覽列「QR Code 解析」。
3. 將加密 QR Code 字串貼入輸入框。
4. 如需使用非預設金鑰，可勾選「使用自訂金鑰」，並貼上 Base64 字串。
5. 按下「開始解析」，畫面會顯示驗證結果與解密後的 JSON。

## 測試建議
- 後端可執行 `mvn test` 驗證 `QrCodeCryptoService` 單元測試。
- 前端可手動貼入範例加密字串確認流程通暢，必要時使用自訂金鑰功能模擬不同環境。
