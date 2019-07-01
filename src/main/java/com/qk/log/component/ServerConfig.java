package com.qk.log.component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: ServerConfig.java
 * @Description: 为了获取请求的IP和端口号
 *
 * @version: v1.0.0
 * @author: AN
 * @date: 2019年3月6日 下午12:57:37 
 * 
 */
@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
	private int serverPort;

	public String getUrl(HttpServletRequest request) {
		String reqIP = getReqIP(request);
		System.out.println("请求的用户IP：" + reqIP);
		if (reqIP.startsWith("0")) {
			return "localhost:" + this.serverPort;
		}
		String ip = getIPV4();
		return ip + ":" + this.serverPort;
	}

	/**
	 * 
	 * @Function: ServerConfig.java
	 * @Description: 动态获取端口号
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月6日 下午12:58:12 
	 */
	@Override
	public void onApplicationEvent(WebServerInitializedEvent event) {
		this.serverPort = event.getWebServer().getPort();
	}

	/**
	 * 
	 * @Function: ServerConfig.java
	 * @Description: 获取本地的外网地址
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月5日 下午7:41:10
	 */
	public String getIPV4() {
		try {
			// 如果请求超时 ,请查看查询本机IP网站 是否更换
			URL url = new URL("http://2018.ip138.com/ic.asp");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			String webContent = "";

			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n");
			}

			br.close();
			webContent = sb.toString();
			int start = webContent.indexOf("[") + 1;
			int end = webContent.indexOf("]");
			webContent = webContent.substring(start, end);
			return webContent;

		} catch (Exception e) {
			System.err.println("获取IP发生错误");
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 
	 * @Function: ServerConfig.java
	 * @Description: 获取客户请求的ip地址，用于检测是否是本地
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月5日 下午7:41:34
	 */
	public String getReqIP(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")) {
					// 根据网卡取本机配置的IP
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
																// = 15
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
		} catch (Exception e) {
			ipAddress = "";
		}
		return ipAddress;
	}

}
