package reactor.netty5.http;

import java.time.Duration;
import reactor.core.publisher.Mono;
import reactor.netty5.resources.ConnectionProvider;
import reactor.netty5.resources.LoopResources;

/**
 * 將 ConnectionProvider 與 LoopResources 合併的簡易 HttpResources。
 */
public final class HttpResources implements ConnectionProvider, LoopResources {

    private static final HttpResources INSTANCE = new HttpResources();

    private HttpResources() {
    }

    public static HttpResources get() {
        return INSTANCE;
    }

    public static Mono<Void> disposeLoopsAndConnectionsLater(Duration quietPeriod, Duration timeout) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> disposeLater() {
        return Mono.empty();
    }

    @Override
    public Mono<Void> disposeLater(Duration quietPeriod, Duration timeout) {
        return Mono.empty();
    }
}
