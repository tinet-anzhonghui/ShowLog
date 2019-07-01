package com.qk.log.bean;

import java.io.Serializable;

/**
 * 
 * @ClassName: FileDirectory.java
 * @Description: 文件目录类
 *
 */
public class FileDirectory implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 唯一标识
	 */
	private String id;
	/**
	 * 父亲
	 */
	private String parent;
	/**
	 * 文本内容，名字
	 */
	private String text;
	/**
	 * 图标
	 */
	private String icon;

	public FileDirectory() {
		super();
	}

	public FileDirectory(String id, String parent, String text, String icon) {
		super();
		this.id = id;
		this.parent = parent;
		this.text = text;
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "FileDirectory [id=" + id + ", parent=" + parent + ", text=" + text + ", icon=" + icon + "]";
	}

}
