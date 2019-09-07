package com.qk.log.util;

import java.io.File;

public interface Constant {

	/**
	 * @Description: 文件相关的常量类
	 * @Author: huihui
	 * @CreateDate: 2019/9/7 16:42
	 */
	interface FileConstant {
		/**
		 * Windows和Linux的斜杠
		 */
		String SLASH = File.separator;
	}

	/**
	 * @Description: 文件类型常量
	 * @Author: huihui
	 * @CreateDate: 2019/9/7 14:45
	 */
	interface FileTypeConstant {
		String file = "file";
		String folder = "folder";
	}
}
