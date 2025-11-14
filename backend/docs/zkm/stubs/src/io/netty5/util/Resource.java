package io.netty5.util;

/**
 * 提供 touch 需要的簡化資源介面。
 */
public interface Resource extends AutoCloseable {

    default Resource touch(Object hint) {
        return this;
    }

    @Override
    default void close() {
        // no-op
    }
}
