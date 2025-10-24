package tw.gov.moda.demohotel.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 繁體中文註解：此組態定義 RestTemplate，供後端呼叫外部模組使用。
 */
@Configuration
public class RestClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientConfig.class);

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
        restTemplate.getInterceptors().add(new LoggingInterceptor());
        return restTemplate;
    }

    private static class LoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            LOGGER.info("外呼 Request: {} {} headers={} body={}",
                    request.getMethod(),
                    request.getURI(),
                    sanitizeHeaders(request.getHeaders()),
                    new String(body, StandardCharsets.UTF_8));
            ClientHttpResponse response = execution.execute(request, body);
            try {
                LOGGER.info("外呼 Response: status={} headers={}",
                        response.getStatusCode(),
                        sanitizeHeaders(response.getHeaders()));
            } catch (IOException ex) {
                LOGGER.warn("外呼 Response 讀取狀態時發生錯誤", ex);
            }
            return response;
        }

        private Object sanitizeHeaders(org.springframework.http.HttpHeaders headers) {
            org.springframework.http.HttpHeaders masked = org.springframework.http.HttpHeaders.writableHttpHeaders(new org.springframework.http.HttpHeaders());
            masked.putAll(headers);
            if (masked.containsKey("Access-Token")) {
                masked.set("Access-Token", "***");
            }
            return masked;
        }
    }
}
