package io.netty5.buffer;

/**
 * 簡化版 ByteCursor，僅供 Spring 反射。
 */
public final class ByteCursor {

    public boolean isReadable() {
        return false;
    }

    public byte readByte() {
        return 0;
    }

    public int process(io.netty5.util.ByteProcessor processor) {
        return 0;
    }
}
