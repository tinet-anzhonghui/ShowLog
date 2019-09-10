package com.qk.log.bean;

import javax.websocket.Session;
/**
 * @ClassName: SessionManage
 * @Description: session的管理类
 * @Author: huihui
 * @CreateDate: 2019/9/10 15:24
 */
public class SessionManage {

    // 唯一标识
    private String sessionId;
    // websocket的session
    private Session session;
    // 跟session绑定的tail进程
    private Process process;

    public SessionManage() {}

    public SessionManage(String sessionId, Session session) {
        this.sessionId = sessionId;
        this.session = session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "SessionManage{" +
                "sessionId='" + sessionId + '\'' +
                ", session=" + session +
                ", process=" + process +
                '}';
    }
}
