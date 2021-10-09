package com.github.chengzhy.basiccode.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocketServer
 * @author chengzhy
 * @date 2021/10/9 15:04
 */
@ServerEndpoint("/websocket/v1/demo")
@Component
@Slf4j
public class WebSocketServer {

    private static AtomicInteger onlineCount = new AtomicInteger(0);

    public static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketServerSet.add(this);
        log.info("新连接加入, sessionId:{}, 当前在线连接数:{}, sessionSet size:{}",
                this.session.getId(),
                onlineCount.incrementAndGet(),
                webSocketServerSet.size());
        sendMessage("WebSocket connection success!");
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("收到客户端消息:{}", message);
        sendMessage("收到: " + message);
    }

    @OnClose
    public void onClose() {
        webSocketServerSet.remove(this);
        log.info("有连接断开, sessionId:{}, 当前在线连接数:{}, sessionSet size:{}",
                session.getId(),
                onlineCount.decrementAndGet(),
                webSocketServerSet.size());
    }

    @OnError
    public void onError(Throwable error) {
        log.error("连接错误, sessionId:{}", session.getId(), error);
    }

    public void sendMessage(String message) {
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    public static void sendMessageToAll(String message) {
        webSocketServerSet.parallelStream().forEach(webSocketServer -> {
            webSocketServer.sendMessage(message);
        });
    }

}
