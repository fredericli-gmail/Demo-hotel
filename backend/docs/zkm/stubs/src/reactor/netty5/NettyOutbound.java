package reactor.netty5;

import io.netty5.buffer.BufferAllocator;
import io.netty5.buffer.DefaultBufferAllocators;
import java.nio.file.Path;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * 最小化 NettyOutbound，僅提供 alloc()。
 */
public class NettyOutbound {

    public BufferAllocator alloc() {
        return DefaultBufferAllocators.preferredAllocator();
    }

    public NettyOutbound send(Publisher<?> publisher) {
        return this;
    }

    public NettyOutbound sendGroups(Publisher<? extends Publisher<?>> groups) {
        return this;
    }

    public NettyOutbound sendFile(Path file, long position, long count) {
        return this;
    }

    public Mono<Void> then() {
        return Mono.empty();
    }
}
