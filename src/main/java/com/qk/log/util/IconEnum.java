package com.qk.log.util;

/**
 * @ClassName: IconEnum.java
 * @Description: 图标的枚举类，使用Font Awesome图标
 * @version: v1.0.0
 * @author: AN
 * @date: 2019年2月27日 下午9:00:27
 */
public enum IconEnum {
    /**
     * 文件夹图标
     */
    Folder("fa fa-folder"),
    /**
     * 文件图标
     */
    File("fa fa-file-text-o");

    private String value;

    IconEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getIconByType(String type) {
        switch (type) {
            case Constant.FileTypeConstant.file:
                return File.value;
            case Constant.FileTypeConstant.folder:
                return Folder.value;
            default:
                return "";
        }
    }

}
