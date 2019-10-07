package com.qk.log.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.qk.log.bean.Tree;

/**
 * @ClassName: FileUtil.java
 * @Description: 读取文件目录的工具类
 * @version: v1.0.0
 * @author: AN
 * @date: 2019年3月6日 下午1:06:04
 */
public class FileUtil {

    public static Map<Integer, Tree> maps = new HashMap<Integer, Tree>();// 用来存放数据
    private static Integer id = 0;// 因为测试使用，当初主键id来用

    public void mytest() {
        String filepath = "E:\\hxInvoicingSystemLogs";// 默认路径，扫描此文件夹下面的所有文件
        try {
            file(filepath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void file(String filepath) throws FileNotFoundException {
        File file = new File(filepath);
        // 1.判断文件
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        // 2.是文件该怎么执行
        if (file.isFile()) {
            // String name = file.getName();
            // System.out.println("文件名称："+name);
            // System.out.println("父文件名称："+file.getParent());
            return;
        }

        // 3.获取文件夹路径下面的所有文件递归调用；
        if (file.isDirectory()) {
            // String name = file.getName();
            String path = file.getAbsolutePath();
            String[] list = file.list();
            // String parent = file.getParent();
            // System.out.println("【文件夹】名称："+name);
            // System.out.println("父文件名称："+parent);
            for (int i = 0; i < list.length; i++) {
                String s = list[i];
                String newFilePath = path + Constant.FileConstant.SLASH + s;// 根据当前文件夹，拼接其下文文件形成新的路径
                file(newFilePath);
            }
        }
    }

    /**
     * @Function: FileUtil.java
     * @Description: 根据文件id初始化文件下面的内容，并存储到map集合中
     * @version: v1.0.0
     * @author: AN
     * @date: 2019年3月6日 下午12:59:33
     */
    public static void file(String filepath, int parentid) throws FileNotFoundException {
        File file = new File(filepath);
        // 1.判断文件
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        // 2.是文件该怎么执行
        if (file.isFile()) {
            String name = file.getName();
            String path = file.getAbsolutePath();
            Tree tree = new Tree(id, name, path, parentid, "file");
            maps.put(id++, tree);
            return;
        }
        // 3.获取文件夹路径下面的所有文件递归调用；
        if (file.isDirectory()) {
            String name = file.getName();
            String path = file.getAbsolutePath();
            Tree tree = new Tree(id, name, path + Constant.FileConstant.SLASH, parentid, "folder");
            maps.put(id++, tree);
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                String s = list[i];
                String newFilePath = path + Constant.FileConstant.SLASH + s;// 根据当前文件夹，拼接其下文文件形成新的路径
                file(newFilePath, tree.getId());
            }
        }
    }

    /**
     * @Function: FileUtil.java
     * @Description: 初始化文件工具类
     * @version: v1.0.0
     * @author: AN
     * @date: 2019年3月6日 下午1:00:08
     */
    public static void clear() {
        // 清空存储文件名称的map
        maps.clear();
        // 初始化文件id
        id = 0;
    }

    /**
     * @Function: FileUtil.java
     * @Description:根据提醒获取文件的完整名称
     * @param: path 提醒名称前的路径；remind 提醒的关键词
     * 例如：/usr/lo ==> path = /usr/ ; remind = lo
     * @return：返回结果描述
     * @throws：异常描述
     * @version: v1.0.0
     * @author: AN
     * @date: 2019年3月6日 下午1:00:30
     */
    public static String getFileNameByRemind(String path, String remind) {
        if (!path.startsWith(Constant.FileConstant.SLASH)) {
            path = Constant.FileConstant.SLASH + path;
        }
        File file = new File(path);
        String[] names = file.list();
        String nameResult = null;
        for (String name : names) {
            if (name.startsWith(remind)) {
                if (nameResult == null) {
                    return name;
                } else {
                    return null;
                }

            }
        }
        return null;
    }

    /**
     * @Function: FileUtil.java
     * @Description: 打印文件存储类map集合
     * @version: v1.0.0
     * @author: AN
     * @date: 2019年3月6日 下午1:01:47
     */
    public static void toStringMaps() {
        for (Entry<Integer, Tree> map : maps.entrySet()) {
            System.out.println("key:" + map.getKey() + ";" + " value:" + map.getValue());
        }
    }
}
