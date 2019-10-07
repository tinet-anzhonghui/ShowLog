package com.qk.log.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.qk.log.component.WebSocketComponent;

import javax.websocket.Session;

/**
 * @ClassName: TailLogThread.java
 * @Description: tail 指令执行线程类
 * @version: v1.0.0
 * @author: AN
 * @date: 2019年3月6日 下午1:03:09
 */
public class TailLogThread extends Thread {

    private BufferedReader reader;
    // 当前进程的session
    private Session session;

    /**
     * @Description: 将文件的输入流读入缓冲区中
     * @param:参数描述
     * @author: AN
     * @date: 2019年3月6日 下午1:03:59
     */
    public TailLogThread(InputStream in, Session session) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.session = session;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                session.getBasicRemote().sendText(line + "<br>");
                // System.out.println("读取到的信息："+line);
                // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                //WebSocketComponent.session.getBasicRemote().sendText(line + "<br>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
