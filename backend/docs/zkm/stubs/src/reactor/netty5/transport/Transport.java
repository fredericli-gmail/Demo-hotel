package reactor.netty5.transport;

import reactor.netty5.resources.LoopResources;

/**
 * Reactor Netty5 Transport 介面 stub。
 */
public interface Transport {

    Transport runOn(LoopResources resources);
}
