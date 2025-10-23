package tw.gov.moda.demohotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 繁體中文註解：系統進入點，啟動 Spring Boot 背後的自動組態與元件掃描機制。
 */
@SpringBootApplication
public class DemoHotelApplication {

    /**
     * 繁體中文註解：main 方法負責啟動應用程式，SpringApplication 會初始化整個 Spring Context。
     *
     * @param args 啟動參數
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoHotelApplication.class, args);
    }
}
