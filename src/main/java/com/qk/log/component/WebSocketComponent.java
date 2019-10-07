package com.qk.log.component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.qk.log.bean.SessionManage;
import org.springframework.stereotype.Component;

/**
 * @Description: websocket组件
 * @author: AN
 * @date: 2018年8月18日 下午3:45:05
 */
@ServerEndpoint(value = "/websocket/{sessionId}")
@Component
public class WebSocketComponent {

    // session的管理类,key是sessionid，value是管理类
    public static Map<String, SessionManage> sessionManageMap = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public static Session session = null;

    /**
     * @param sessionId 自动生成的会话id
     * @param session   会话
     * @Description: 连接建立成功调用的方法
     * @author: AN
     * @date: 2018年8月18日 下午3:45:05
     */
    @OnOpen
    public void onOpen(@PathParam("sessionId") String sessionId, Session session) {
        // 新逻辑，放入统一的map进行管理
        WebSocketComponent.sessionManageMap.put(sessionId, new SessionManage(sessionId, session));

        // 原逻辑
//		System.out.println("建立websocket连接了，赋值session");
//		WebSocketComponent.session = session;
        try {
            sendMessage(session, "success<br/>");
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * @Description: 连接关闭调用的方法
     * @author: AN
     * @date: 2018年8月18日 下午3:45:05
     */
    @OnClose
    public void onClose() {
        System.out.println("连接关闭了");
    }

    /**
     * @param message 客户端发送过来的消息
     * @Description: 收到客户端消息后调用的方法
     * @author: AN
     * @date: 2018年8月18日 下午3:45:05
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }

    /**
     * @Description: 发生错误时调用
     * @author: AN
     * @date: 2018年8月18日 下午3:45:05
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            System.out.println("IO异常，重新建立连接");
        } catch (Exception e) {

        }
    }

    /**
     * @Description: 发送消息
     * @author: AN
     * @date: 2018年8月18日 下午3:45:05
     */
    public void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}
