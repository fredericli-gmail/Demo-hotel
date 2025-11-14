package org.eclipse.jetty.websocket.core.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.websocket.core.WebSocketComponents;

/**
 * 最小化的 Jetty WebSocketServerComponents stub，供 ZKM 解析 Spring Boot AutoConfig。
 */
public final class WebSocketServerComponents {

    private WebSocketServerComponents() {
    }

    public static WebSocketComponents ensureWebSocketComponents(Server server, ContextHandler handler) {
        return new WebSocketComponents();
    }
}
