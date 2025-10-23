# Demo Hotel – 飯店電子房卡系統

## 專案簡介
本專案示範如何在飯店內部建置電子房卡（Verifiable Credential, VC）發行與驗證平台，整合 Spring Boot 後端與 Vue 3 + Vite 前端，並呼叫數位發展部提供的 DWVC / DWVP API。

## 專案結構
- `backend/`：Spring Boot 3 服務，負責發行端與驗證端 API 串接，資料持久化使用 JPA。
- `frontend/`：Vue 3 + Vite 操作介面，提供櫃檯人員進行發卡、查詢與狀態管理。
- `docs/`：紀錄開發注意事項與代理操作規範。

## 環境需求
- JDK 17
- Maven 3.9+
- Node.js 20 以上與 npm

## 本地開發流程
1. 進入 `backend/`，執行 `mvn spring-boot:run`（首次需先申請 Access Token 並填入 `application.yml`）。
2. 進入 `frontend/`，執行 `npm install` 後執行 `npm run dev` 啟動前端。
3. 前端預設走 `http://localhost:5173`，透過 Vite 代理呼叫後端 `http://localhost:8080/api`。

## 注意事項
- 設定檔統一使用 `application.yml`，請勿新增 `.properties` 檔案。
- 禁用 Lombok 與 Java Lambda，程式碼以傳統寫法維持可讀性。
- 文件與註解一律採臺灣繁體中文，並保持語氣專業。
- 更多代辦與限制請參考 `docs/codex-reminders.md`。
