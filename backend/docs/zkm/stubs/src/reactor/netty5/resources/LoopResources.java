package reactor.netty5.resources;

import java.time.Duration;
import reactor.core.publisher.Mono;

/**
 * 簡化 LoopResources，提供預設常數與釋放方法。
 */
public interface LoopResources {

    long DEFAULT_SHUTDOWN_QUIET_PERIOD = 2L;
    long DEFAULT_SHUTDOWN_TIMEOUT = 15L;

    static LoopResources create(String prefix) {
        return new SimpleLoopResources();
    }

    Mono<Void> disposeLater(Duration quietPeriod, Duration timeout);

    final class SimpleLoopResources implements LoopResources {

        @Override
        public Mono<Void> disposeLater(Duration quietPeriod, Duration timeout) {
            return Mono.empty();
        }
    }
}
