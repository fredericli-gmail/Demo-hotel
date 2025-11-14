package io.netty5.buffer;

import io.netty5.util.Resource;
import io.netty5.util.Send;

/**
 * 簡化版 Netty5 Buffer 實作，只保留必要介面。
 */
public class Buffer {

    private static final Resource NOOP_RESOURCE = new Resource() { };

    private final byte[] data;

    Buffer() {
        this(new byte[0]);
    }

    Buffer(byte[] source) {
        this.data = source == null ? new byte[0] : source.clone();
    }

    public byte[] toByteArray() {
        return data.clone();
    }

    public int readableBytes() {
        return data.length;
    }

    public Buffer readBytes(byte[] destination, int destIndex, int length) {
        if (destination != null && destIndex >= 0 && length > 0) {
            int copyLength = Math.min(length, Math.max(0, destination.length - destIndex));
            System.arraycopy(data, 0, destination, destIndex, Math.min(copyLength, data.length));
        }
        return this;
    }

    public void close() {
        // no-op
    }

    public int writerOffset() {
        return data.length;
    }

    public ByteCursor openCursor(int from, int to) {
        return new ByteCursor();
    }

    public int writableBytes() {
        return 0;
    }

    public int readerOffset() {
        return 0;
    }

    public Buffer readerOffset(int newOffset) {
        return this;
    }

    public Buffer writerOffset(int newOffset) {
        return this;
    }

    public byte getByte(int index) {
        if (index >= 0 && index < data.length) {
            return data[index];
        }
        return 0;
    }

    public int capacity() {
        return data.length;
    }

    public Buffer ensureWritable(int size) {
        return this;
    }

    public byte readByte() {
        return data.length > 0 ? data[0] : 0;
    }

    public Buffer writeByte(byte value) {
        return this;
    }

    public Buffer writeBytes(byte[] source) {
        return this;
    }

    public Buffer writeBytes(byte[] source, int srcIndex, int length) {
        return this;
    }

    public Buffer writeBytes(java.nio.ByteBuffer buffer) {
        return this;
    }

    public Buffer writeBytes(Buffer buffer) {
        return this;
    }

    public Buffer writeCharSequence(CharSequence sequence, java.nio.charset.Charset charset) {
        return this;
    }

    public Buffer copy(int index, int length) {
        return new Buffer();
    }

    public Buffer split(int length) {
        return new Buffer();
    }

    public Buffer split() {
        return new Buffer();
    }

    public boolean isDirect() {
        return false;
    }

    public void copyInto(int srcIndex, java.nio.ByteBuffer destination, int destIndex, int length) {
        // no-op
    }

    public void copyInto(int srcIndex, byte[] destination, int destIndex, int length) {
        if (destination == null || length <= 0) {
            return;
        }
        int actualSrcIndex = Math.max(0, srcIndex);
        int actualLength = Math.min(length, Math.max(0, data.length - actualSrcIndex));
        int actualDestIndex = Math.max(0, destIndex);
        int availableDest = Math.max(0, destination.length - actualDestIndex);
        int copyLength = Math.min(actualLength, availableDest);
        if (copyLength > 0) {
            System.arraycopy(data, actualSrcIndex, destination, actualDestIndex, copyLength);
        }
    }

    public ComponentIterator forEachComponent() {
        return ComponentIterator.empty();
    }

    public String toString(java.nio.charset.Charset charset) {
        java.nio.charset.Charset useCharset = charset != null
            ? charset
            : java.nio.charset.StandardCharsets.UTF_8;
        return new String(data, useCharset);
    }

    public Resource touch(Object hint) {
        return NOOP_RESOURCE;
    }

    public Send send() {
        return new Send() {
            @Override
            public Object receive() {
                return Buffer.this;
            }
        };
    }
}
