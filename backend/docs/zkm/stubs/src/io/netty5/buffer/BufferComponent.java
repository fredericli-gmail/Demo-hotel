package io.netty5.buffer;

import java.nio.ByteBuffer;

/**
 * 提供可讀／可寫 ByteBuffer 的簡化元件。
 */
public interface BufferComponent extends ComponentIterator.Next {

    BufferComponent EMPTY = new BufferComponent() {
        @Override
        public ByteBuffer readableBuffer() {
            return ByteBuffer.allocate(0);
        }

        @Override
        public ByteBuffer writableBuffer() {
            return ByteBuffer.allocate(0);
        }
    };

    @Override
    default ByteBuffer readableBuffer() {
        return ByteBuffer.allocate(0);
    }

    @Override
    default ByteBuffer writableBuffer() {
        return ByteBuffer.allocate(0);
    }
}
