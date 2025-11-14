package io.netty5.buffer;

/**
 * Netty 5 API shim，提供 Spring 需用的 DefaultBufferAllocators 類別。
 */
public final class DefaultBufferAllocators {

    private static final BufferAllocator INSTANCE = new NoOpBufferAllocator();

    private DefaultBufferAllocators() {
    }

    public static BufferAllocator preferredAllocator() {
        return INSTANCE;
    }

    private static final class NoOpBufferAllocator implements BufferAllocator {

        @Override
        public void close() {
            // no-op
        }
    }
}
