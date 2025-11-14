package io.netty5.buffer;

/**
 * 精簡版 Netty 5 BufferAllocator 介面，供 Spring 反射使用。
 */
public interface BufferAllocator extends AutoCloseable {

    /**
     * 建立可供 Spring 解碼使用的封包緩衝。
     */
    default Buffer copyOf(byte[] source) {
        return new Buffer(source);
    }

    default Buffer copyOf(java.nio.ByteBuffer source) {
        if (source == null) {
            return new Buffer();
        }
        java.nio.ByteBuffer duplicate = source.slice();
        byte[] data = new byte[duplicate.remaining()];
        duplicate.get(data);
        return new Buffer(data);
    }

    default Buffer allocate(int size) {
        int actualSize = Math.max(0, size);
        return new Buffer(new byte[actualSize]);
    }

    default CompositeBuffer compose() {
        return new CompositeBuffer();
    }

    default AllocationType getAllocationType() {
        return AllocationType.HEAP;
    }

    @Override
    default void close() {
        // no-op
    }
}
