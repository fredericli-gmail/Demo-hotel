package org.eclipse.jetty.ee10.websocket.server;

import jakarta.servlet.ServletContext;

/**
 * 提供給 ZKM 參考用的 Jetty WebSocket 容器 stub。
 */
public final class JettyWebSocketServerContainer {

    private JettyWebSocketServerContainer() {
    }

    public static JettyWebSocketServerContainer getContainer(ServletContext servletContext) {
        return new JettyWebSocketServerContainer();
    }

    public static JettyWebSocketServerContainer ensureContainer(ServletContext servletContext) {
        return new JettyWebSocketServerContainer();
    }
}
