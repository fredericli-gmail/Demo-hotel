package org.apache.pulsar.reactive.client.producercache;

import java.time.Duration;

/**
 * ZKM 解析 Pulsar Reactive 自動設定所需的空實作。
 */
public class CaffeineShadedProducerCacheProvider {

    public CaffeineShadedProducerCacheProvider(Duration ttl,
                                               Duration cleanupInterval,
                                               Long maximumSize,
                                               Integer initialCapacity) {
        // 僅供 ZKM 解析使用，無需實作。
    }
}
