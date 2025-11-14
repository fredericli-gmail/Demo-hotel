package reactor.netty5.http.client;

import io.netty5.handler.codec.http.headers.DefaultHttpCookiePair;
import io.netty5.handler.codec.http.headers.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

/**
 * 簡化版 HttpClientRequest，只提供 Spring 操作 Header 與 Cookie。
 */
public final class HttpClientRequest {

    private final HttpHeaders headers = new HttpHeaders();
    private final List<io.netty5.handler.codec.http.headers.HttpCookiePair> cookies = new ArrayList<>();

    public HttpHeaders requestHeaders() {
        return headers;
    }

    public HttpClientRequest addCookie(DefaultHttpCookiePair cookie) {
        return addCookie((io.netty5.handler.codec.http.headers.HttpCookiePair) cookie);
    }

    public HttpClientRequest addCookie(io.netty5.handler.codec.http.headers.HttpCookiePair cookie) {
        if (cookie != null) {
            cookies.add(cookie);
        }
        return this;
    }
}
