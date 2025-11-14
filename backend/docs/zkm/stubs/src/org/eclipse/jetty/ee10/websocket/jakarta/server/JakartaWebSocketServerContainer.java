package org.eclipse.jetty.ee10.websocket.jakarta.server;

import jakarta.servlet.ServletContext;

/**
 * 供 ZKM 解析之 Jetty Jakarta WebSocket 容器 stub。
 */
public final class JakartaWebSocketServerContainer {

    private JakartaWebSocketServerContainer() {
    }

    public static JakartaWebSocketServerContainer getContainer(ServletContext servletContext) {
        return new JakartaWebSocketServerContainer();
    }

    public static JakartaWebSocketServerContainer ensureContainer(ServletContext servletContext) {
        return new JakartaWebSocketServerContainer();
    }
}
