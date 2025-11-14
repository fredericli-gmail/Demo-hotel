package io.netty5.buffer;

/**
 * 簡化版配置型態。
 */
public interface AllocationType {

    AllocationType HEAP = () -> false;
    AllocationType DIRECT = () -> true;

    boolean isDirect();
}
