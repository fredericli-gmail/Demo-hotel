# Repository Guidelines

## 專案架構與模組分工
根目錄僅放授權與說明文件（`LICENSE`、`README.md`、`AGENTS.md`、`docs/`），來源碼分為兩個模組：`backend/` 為 Spring Boot 服務，採 MVC 與分層服務（`controller/`、`service/`、`repository/`）；`frontend/` 為 Vue 3 + Vite 介面，頁面放在 `src/views/`，共用元件放在 `src/components/`。資料存取使用 JPA，測試程式建議與來源同層，如 `backend/src/test/java/...`。請將任何新草稿或架構圖集中於 `docs/`，不得散落其他資料夾。

## 建置、測試與開發指令
後端：
- `mvn spring-boot:run` 啟動 API（記得先在 `application.yml` 填入 Access-Token）。
- `mvn test` 執行 JUnit 測試。
前端：
- `npm install` 安裝相依套件。
- `npm run dev` 啟動本機開發伺服器（預設 http://localhost:5173）。
- `npm run build` 打包靜態檔案。
若需代理至其他後端位址，調整 `frontend/vite.config.js` 之 proxy 設定。

## 程式風格與命名規範
Java 以兩空白縮排，禁止使用 Lambda 或 Stream；請維持傳統 for 迴圈與清楚的段落註解。禁止引入 Lombok。Vue 採 Options API，檔名使用 kebab-case（例：`issue-page.vue`）。變數與函式使用 camelCase，類別與元件名稱採 PascalCase。所有註解與 UI 文案均需使用臺灣繁體中文。

## 測試方針
後端以 JUnit 5 撰寫單元／服務層測試，命名建議 `*ServiceTest.java`；若 Mock 外部 API，請提供固定回應樣板。前端可使用 Vitest 或 Cypress，檔名建議 `*.spec.ts`。合併前務必確保 `mvn test` 與 `npm run build` 通過，並在 PR 中記錄測試命令與結果。

## Commit 與 Pull Request 指引
採 Conventional Commits（例如 `feat: 新增發卡 API`、`fix: 修正查詢參數`），訊息主旨不超過 72 字元。PR 需說明：變更摘要、關聯議題、測試結果、影響範圍及需注意的設定（如 Access-Token）。若有畫面或流程變更，提供截圖或簡短錄影，並待 CI 綠燈再請 reviewer。*** End Patch
