package io.netty5.buffer;

import io.netty5.util.Send;

/**
 * 只提供 extendWith 的簡易 CompositeBuffer。
 */
public class CompositeBuffer extends Buffer {

    public CompositeBuffer() {
        super();
    }

    public CompositeBuffer extendWith(Send send) {
        if (send != null) {
            send.close();
        }
        return this;
    }
}
