package io.netty5.util;

/**
 * 用於 Buffer 傳遞的最小化 Send 介面。
 */
public interface Send extends AutoCloseable {

    default Object receive() {
        return null;
    }

    @Override
    default void close() {
        // no-op
    }
}
