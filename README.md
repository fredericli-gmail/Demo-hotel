# Demo Hotel – 飯店電子房卡系統

## 專案簡介
本專案示範如何在飯店內部建置電子房卡（Verifiable Credential, VC）發行與驗證平台，整合 Spring Boot 後端與 Vue 3 + Vite 前端，並呼叫數位發展部提供的 DWVC / DWVP API。

## 專案結構
- `backend/`：Spring Boot 3 服務，負責發行端與驗證端 API 串接，資料持久化使用 PostgreSQL。
- `frontend/`：Vue 3 + Vite 操作介面，提供櫃檯人員進行發卡、查詢與狀態管理。
- `docs/`：紀錄開發注意事項與代理操作規範。
- `docker-compose.yml`：整合前端、後端與 PostgreSQL 的容器編排設定。

## 環境需求
- JDK 17
- Maven 3.9+
- Node.js 20 以上與 npm
- Docker 24+ 與 Docker Compose v2

## 本地開發流程
1. 進入 `backend/`，執行 `mvn spring-boot:run`（首次需先申請 Access Token 並填入 `application.yml`）。
2. 進入 `frontend/`，執行 `npm install` 後執行 `npm run dev` 啟動前端。
3. 前端預設走 `http://localhost:5173`，透過 Vite 代理呼叫後端 `http://localhost:8080/api`。

## 介面功能
- 發卡作業 `/issue`：產生房卡 VC，支援入住／退房日期自動帶入。
- 早餐券發放 `/tickets/breakfast`：使用 credentialType `00000000_hlbft1023` 發送早餐券，可填入房號、類別與餐廳位置。
- 發卡查詢 `/inquiry`：依交易序號或標籤查詢 VC。
- 卡片管理 `/lifecycle`：執行停止、啟用等狀態調整。
- 驗證工具 `/verification`：整合驗證端 API 供前台快速檢視。

## Docker Compose 快速啟動
1. 編輯 `docker-compose.yml`，將 `WALLET_ISSUER_ACCESS_TOKEN` 與 `WALLET_VERIFIER_ACCESS_TOKEN` 改為實際取得的值。
2. 於專案根目錄執行：
   ```bash
   docker compose up --build
   ```
3. 服務啟動後：
   - 前端入口：http://localhost:5173
   - 後端 API：http://localhost:8080/api
   - PostgreSQL：`postgres://demohotel:demohotel@localhost:5432/demohotel`

若需停止服務，可執行 `docker compose down`，資料庫使用 named volume `db_data` 保存資料。

## 注意事項
- 設定檔統一使用 `application.yml`，請勿新增 `.properties` 檔案。
- 禁用 Lombok 與 Java Lambda，程式碼以傳統寫法維持可讀性。
- 文件與註解一律採臺灣繁體中文，並保持語氣專業。
- 更多代辦與限制請參考 `docs/codex-reminders.md`。
