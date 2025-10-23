# 開發與測試指引

## 後端（Spring Boot）
- 使用 `backend/pom.xml` 管理依賴，維持 JDK 17 與 Spring Boot 3.2.x。
- 設定檔請更新 `backend/src/main/resources/application.yml` 中的發行端與驗證端 Access Token。
- 本地啟動：
  ```bash
  mvn spring-boot:run
  ```
- 若需切換資料庫，可在 `application.yml` 新增對應的 profile，並避免建立 `.properties` 檔案。
- 測試建議：利用 `mvn test` 撰寫並執行單元測試；在沙盒環境受限時，可使用 mock 物件模擬外部 API。

## 前端（Vue 3 + Vite）
- 進入 `frontend/` 後執行：
  ```bash
  npm install
  npm run dev
  ```
- 開發時透過 Vite 代理呼叫後端 API，正式環境可改為透過 Nginx 轉發。
- UI 與文案皆需採繁體中文，若有共用字詞請整理於 `src/styles` 或常數檔案中。

## API 串接注意事項
- 依照數位發展部規格文件，呼叫 DWVC / DWVP 時務必帶上 `Access-Token`。
- 所有可選欄位在發送前應再次檢查是否需要填寫，避免出現 `缺少參數或參數不合法` 錯誤。
- 交易序號、CID、DataTag 需妥善儲存於資料庫以便追蹤。

## 版本控管
- 本代理不會替您執行 `git commit`，請在確認修改後自行提交，建議依功能切分 commit。
- Commit 訊息建議採 `feat:`, `fix:` 等前綴，維持歷史紀錄清晰。
