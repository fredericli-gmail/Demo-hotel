package io.netty5.buffer;

import java.nio.ByteBuffer;

/**
 * 最小化版本的 Netty5 ComponentIterator，只提供 Spring 需用的介面。
 */
public interface ComponentIterator extends AutoCloseable {

    ComponentIterator.Next firstReadable();

    ComponentIterator.Next firstWritable();

    @Override
    default void close() {
        // no-op
    }

    /**
     * 單一元素空實作，避免 Spring 呼叫時丟錯。
     */
    ComponentIterator EMPTY = new ComponentIterator() {
        @Override
        public Next firstReadable() {
            return BufferComponent.EMPTY;
        }

        @Override
        public Next firstWritable() {
            return BufferComponent.EMPTY;
        }
    };

    static ComponentIterator empty() {
        return EMPTY;
    }

    interface Next {

        default ComponentIterator.Next nextReadable() {
            return null;
        }

        default ComponentIterator.Next nextWritable() {
            return null;
        }

        default ByteBuffer readableBuffer() {
            return ByteBuffer.allocate(0);
        }

        default ByteBuffer writableBuffer() {
            return ByteBuffer.allocate(0);
        }
    }
}
