package org.eclipse.jetty.websocket.core.server;

import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * 供 ZKM 參考的 WebSocketMappings stub。
 */
public final class WebSocketMappings {

    private WebSocketMappings() {
    }

    public static WebSocketMappings ensureMappings(ContextHandler handler) {
        return new WebSocketMappings();
    }
}
