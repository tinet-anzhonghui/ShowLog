package com.qk.log.component;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * 
 * @Description: websocket组件
 *
 * @author: AN
 * @date: 2018年8月18日 下午3:45:05
 * 
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocketComponent {

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	public static Session session = null;

	/**
	 * 
	 * @Description: 连接建立成功调用的方法
	 *
	 * @author: AN
	 * @date: 2018年8月18日 下午3:45:05
	 * @param websocketType
	 *            客户端发送过来的websocket连接类型
	 * @param session
	 *            会话
	 * 
	 */
	@OnOpen
	public void onOpen(@PathParam("websocketType") String websocketType, Session session) {
		
		System.out.println("建立websocket连接了，赋值session");
		WebSocketComponent.session = session;
    	
		try {
			sendMessage("success<br/>");
		} catch (IOException e) {
			System.out.println("IO异常");
		}
	}

	/**
	 * 
	 * @Description: 连接关闭调用的方法
	 *
	 * @author: AN
	 * @date: 2018年8月18日 下午3:45:05
	 * 
	 */
	@OnClose
	public void onClose() {
		System.out.println("连接关闭了");
	}

	/**
	 * 
	 * @Description: 收到客户端消息后调用的方法
	 *
	 * @author: AN
	 * @date: 2018年8月18日 下午3:45:05
	 * @param message
	 *            客户端发送过来的消息
	 * 
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("来自客户端的消息:" + message);
	}

	/**
	 * 
	 * @Description: 发生错误时调用
	 *
	 * @author: AN
	 * @date: 2018年8月18日 下午3:45:05
	 * 
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		try {
			System.out.println("IO异常，重新建立连接");
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @Description: 发送消息
	 *
	 * @author: AN
	 * @date: 2018年8月18日 下午3:45:05
	 * 
	 */
	public void sendMessage(String message) throws IOException {
		WebSocketComponent.session.getBasicRemote().sendText(message);
	}

}
