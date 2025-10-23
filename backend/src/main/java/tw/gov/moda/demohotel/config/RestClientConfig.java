package tw.gov.moda.demohotel.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 繁體中文註解：此組態定義 RestTemplate，供後端呼叫外部模組使用。
 */
@Configuration
public class RestClientConfig {

    /**
     * 繁體中文註解：建立共用的 RestTemplate，設定逾時時間並保留訊息內容方便除錯。
     *
     * @param builder Spring 自動注入的建置器
     * @return RestTemplate 實例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(15))
                .build();
        BufferingClientHttpRequestFactory bufferingFactory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        restTemplate.setRequestFactory(bufferingFactory);
        return restTemplate;
    }
}
