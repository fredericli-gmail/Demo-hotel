package io.netty5.util;

/**
 * 最小化的 ByteProcessor 介面，供 Spring 使用 Netty5 stub。
 */
@FunctionalInterface
public interface ByteProcessor {

    boolean process(byte value);
}
