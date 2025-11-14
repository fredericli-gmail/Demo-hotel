package reactor.netty5.resources;

import reactor.core.publisher.Mono;

/**
 * 精簡版 Netty5 ConnectionProvider，僅提供 Spring 需要的 API。
 */
public interface ConnectionProvider {

    static ConnectionProvider create(String name, int maxConnections) {
        return new SimpleConnectionProvider();
    }

    static ConnectionProvider create(String name) {
        return new SimpleConnectionProvider();
    }

    Mono<Void> disposeLater();

    /**
     * 無實作的基本 provider。
     */
    final class SimpleConnectionProvider implements ConnectionProvider {

        @Override
        public Mono<Void> disposeLater() {
            return Mono.empty();
        }
    }
}
