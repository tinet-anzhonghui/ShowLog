package com.qk.log.bean;

/**
 * 
 * @ClassName: Tree.java
 * @Description: 读取的文件树状结构
 *
 */
public class Tree {
	private Integer id;
	private String name;// 文件夹或者文件名称
	private String path;// 全路径,或则部分路径,自己决定
	private Integer parentId;// 父节点id
	private String type; // 文件类型

	public Tree() {
	}

	public Tree(Integer id, String name, String path, Integer parentId, String type) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.parentId = parentId;
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Tree [id=" + id + ", name=" + name + ", path=" + path + ", parentId=" + parentId + ", type=" + type
				+ "]";
	}

}
