package reactor.netty5.http.client;

import java.net.URI;
import java.util.function.BiFunction;
import io.netty5.handler.codec.http.HttpMethod;
import reactor.core.publisher.Flux;
import reactor.netty5.resources.ConnectionProvider;
import reactor.netty5.resources.LoopResources;
import reactor.netty5.transport.Transport;

/**
 * Reactor Netty5 HttpClient 的極簡替身，僅保留 Spring 需要的鏈式 API。
 */
public class HttpClient implements Transport {

    public static HttpClient create() {
        return new HttpClient();
    }

    public static HttpClient create(ConnectionProvider provider) {
        return new HttpClient();
    }

    public HttpClient wiretap(boolean enabled) {
        return this;
    }

    public HttpClient compress(boolean enabled) {
        return this;
    }

    public RequestSender request(HttpMethod method) {
        return new DefaultRequestSender();
    }

    @Override
    public Transport runOn(LoopResources resources) {
        return this;
    }

    private static final class DefaultRequestSender implements RequestSender, ResponseReceiver {

        @Override
        public UriConfiguration uri(URI uri) {
            return this;
        }

        @Override
        public UriConfiguration uri(String uri) {
            return this;
        }

        @Override
        public ResponseReceiver send(BiFunction<?, ?, ?> sender) {
            return this;
        }

        @Override
        public Flux responseConnection(BiFunction<?, ?, ?> receiver) {
            return Flux.empty();
        }
    }

    public interface UriConfiguration {
        UriConfiguration uri(URI uri);
        UriConfiguration uri(String uri);
    }

    public interface RequestSender extends UriConfiguration {
        ResponseReceiver send(BiFunction<?, ?, ?> sender);
    }

    public interface ResponseReceiver {
        Flux responseConnection(BiFunction<?, ?, ?> receiver);
    }
}
