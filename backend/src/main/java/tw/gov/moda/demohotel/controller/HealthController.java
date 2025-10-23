package tw.gov.moda.demohotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 繁體中文註解：提供簡易健康檢查端點，方便部署後確認服務是否存活。
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * 繁體中文註解：回傳固定訊息，代表系統可對外提供服務。
     *
     * @return ResponseEntity<String> 健康狀態
     */
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("服務正常");
    }
}
