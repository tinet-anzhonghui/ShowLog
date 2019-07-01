package com.qk.log.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qk.log.bean.FileDirectory;
import com.qk.log.bean.Tree;
import com.qk.log.component.ServerConfig;
import com.qk.log.util.Constant;
import com.qk.log.util.FileUtil;
import com.qk.log.util.GsonUtil;
import com.qk.log.util.IconEnum;
import com.qk.log.util.TailLogThread;

@Controller
public class LogController {

	// 进程
	private Process process;
	// 输入流
	private InputStream inputStream;

	
	@Autowired
	private ServerConfig serverConfig;

	/**
	 * 
	 * @Function: LogController.java
	 * @Description: 初始化文件的路径，用于显示树状图
	 *
	 * @param:filePath 文件路径
	 * @return：树状图的json格式，需要注意的事需要制定返回值的格式，通过produces属性
	 * @throws：异常描述
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月6日 下午12:50:21 
	 *
	 */
	@RequestMapping(value = "/initLog", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String initLog(String filePath) throws FileNotFoundException {
		System.out.println("初始化的日志路径：" + filePath);

		if (filePath == null || "".equals(filePath) || filePath.length() == 0) {
			return null;
		}

		// 读取路径内容
		// FileUtil.toStringMaps();
		FileUtil.clear(); // 清空存储文件的map集合
		// FileUtil.toStringMaps();
		FileUtil.file(filePath, 0);
		FileUtil.toStringMaps();

		List<FileDirectory> listVO = new ArrayList<FileDirectory>();
		Tree root = FileUtil.maps.get(0);
		listVO.add(new FileDirectory(root.getId().toString(), "#", root.getName(),
				IconEnum.getIconByType(root.getType())));
		// 从根节点往后追加节点
		for (int i = 1; i < FileUtil.maps.size(); i++) {
			Tree tree = FileUtil.maps.get(i);
			// System.out.println("id:" + tree.getId() + " parentId:" +
			// tree.getParentId() + "==" + tree.getPath());
			listVO.add(new FileDirectory(tree.getId().toString(), tree.getParentId().toString(), tree.getName(),
					IconEnum.getIconByType(tree.getType())));
		}

		System.out.println(GsonUtil.BeanToGson(listVO));
		return GsonUtil.BeanToGson(listVO);
	}

	/**
	 * 
	 * @Function: LogController.java
	 * @Description: tab键的提醒
	 *
	 * @param:filePath 文件路径
	 * @return：匹配后的文件路径
	 * @throws：异常描述
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月6日 下午12:51:54 
	 *
	 */
	@RequestMapping(value = "/remind")
	@ResponseBody
	public String remind(String filePath) {
		System.out.println("文件路径：" + filePath);

		int startIndex = filePath.lastIndexOf(Constant.SLASH);
		if (startIndex == -1) {
			return null;
		}
		int endIndex = filePath.length();
		String remind = filePath.substring(startIndex + 1, endIndex);
		String path = filePath.substring(0, startIndex);

		if (remind == null) {
			return null;
		}

		String fileName = FileUtil.getFileNameByRemind(path, remind);
		if (fileName == null) {
			return null;
		}
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(path);
		sBuffer.append(Constant.SLASH);
		sBuffer.append(fileName);
		if (fileName.lastIndexOf(".") == -1) {
			sBuffer.append(Constant.SLASH);
		}
		System.out.println(sBuffer.toString());
		return sBuffer.toString();
	}

	/**
	 * 
	 * @Function: LogController.java
	 * @Description: 该函数的功能描述
	 *
	 * @param:id 要查看的文件的唯一标识;displayRowNum tail指令需要显示的行数
	 * @return：返回结果描述
	 * @throws：异常描述
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月6日 下午12:52:44 
	 *
	 */
	@RequestMapping(value = "/startTail")
	public void startTail(String id, Integer displayRowNum) {
		if (id == null) {
			return;
		}

		Tree tree = FileUtil.maps.get(Integer.valueOf(id));
		String command = null;
		// 处理显示行数，拼接指令
		if (displayRowNum == null || displayRowNum == 0) {
			command = "tail -n +0 -f " + tree.getPath();// 显示所有内容
		} else {
			command = "tail -f -n " + displayRowNum + " " + tree.getPath(); // 显示末尾几行
		}
		System.out.println(command);

		try {
			// 执行tail -f命令 （杀死上一个线程）
			if (process != null) {
				process.destroy();
			}
			process = Runtime.getRuntime().exec(command);
			inputStream = process.getInputStream();

			// 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
			TailLogThread thread = new TailLogThread(inputStream);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Function: LogController.java
	 * @Description: 获取本机的外网IP，主要是为了处理${pageContext.request.contextPath}属性在jsp页面不好用，可能是springboot的原因
	 *
	 * @param:描述1描述
	 * @return：返回结果描述
	 * @throws：异常描述
	 *
	 * @version: v1.0.0
	 * @author: AN
	 * @date: 2019年3月6日 下午12:55:03 
	 *
	 */
	@RequestMapping("/getIPV4")
	@ResponseBody
	public String getIPV4(HttpServletRequest request) {
		String contextPath = serverConfig.getUrl(request);
		System.out.println("本机的外网ip和端口：" + contextPath);
		return contextPath;
	}
}
