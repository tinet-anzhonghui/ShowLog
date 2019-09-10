package com.qk.log.util;

import com.qk.log.bean.Tree;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @ClassName: AddConfigUtil
 * @Description: 额外配置工具类
 * @Author: huihui
 * @CreateDate: 2019/9/6 16:14
 */
@Component
public class AddConfigUtil {

    @Value("${no-permission.filter.file}")
    private String filterFiles;
    @Value("${no-permission.filter.directory}")
    private String filterDirectorys;
    @Value("${no-permission.filter.suffix}")
    private String filterSuffixs;
    @Value("${no-permission.filter.prefix}")
    private String filterPrefixs;

    private List<String> filterFileList = new ArrayList<>();;
    private List<String> filterDirectoryList = new ArrayList<>();
    private List<String> filterSuffixList = new ArrayList<>();
    private List<String> filterPrefixList = new ArrayList<>();;

    public static String addConfigPath = "";

    static {
        addConfigPath = System.getProperty("user.dir") + File.separator + "show-log" + File.separator
                + "add-config.yml";
    }

    /**
     * @Description: 初始化过滤文件
     * @param
     * @return:
     * @Author: huihui
     * @CreateDate: 2019/9/7 15:41
     */
    public void init() {
        // 前后顺序很重要，第一种决定结果
        if (CollectionUtils.isNull(filterFileList) && StringUtils.isNotEmpty(filterFiles)) {
            // 文件前面添加分隔符，避免文件夹名称跟文件名称相同
            filterFileList = Arrays.asList(filterFiles.split(","));
        }
        if (CollectionUtils.isNull(filterDirectoryList) && StringUtils.isNotEmpty(filterDirectorys)) {
            // 文件前面添加分隔符，避免文件夹名称跟文件名称相同
            String[] directorys = filterDirectorys.split(",");
            for (String directory : directorys) {
                filterDirectoryList.add(directory + Constant.FileConstant.SLASH);
            }
        }
        if (CollectionUtils.isNull(filterSuffixList) && StringUtils.isNotEmpty(filterSuffixs)) {
            filterSuffixList = Arrays.asList(filterSuffixs.split(","));
        }
        if (CollectionUtils.isNull(filterPrefixList) && StringUtils.isNotEmpty(filterPrefixs)) {
            filterPrefixList = Arrays.asList(filterPrefixs.split(","));
        }
    }

    /**
     * @param treeMap 树集合
     * @Description: 文件的过滤，由效率问题，后期考虑有没有优化方法 TODO
     * @return:
     * @Author: huihui
     * @CreateDate: 2019/9/7 14:23
     */
    public void performFilter(Map<Integer, Tree> treeMap) {
        // 初始化过滤的内容，这样可以保证获取的过滤内容是最新的
        init();
        // 遍历树，使用迭代器的方法，因为要删除元素
        Iterator<Map.Entry<Integer, Tree>> treeIterator = treeMap.entrySet().iterator();
        start: while (treeIterator.hasNext()) {
            Map.Entry<Integer, Tree> entry = treeIterator.next();
            Tree value = entry.getValue();
            // 过滤文件夹
            for (String directory : filterDirectoryList) {
                if (value.getPath().indexOf(directory) != -1) {
                    treeIterator.remove();
                    continue start;
                }
            }

            // 文件夹处理完，继续下一个
            if (Constant.FileTypeConstant.folder.equals(value.getType())) {
                continue;
            }

            // 过滤文件
            for (String file : filterFileList) {
                if (Objects.equals(file, value.getName())) {
                    treeIterator.remove();
                    continue start;
                }
            }

            // 过滤后缀
            for (String suffix : filterSuffixList) {
                int lastIndex = value.getName().lastIndexOf(suffix);
                if (lastIndex != -1 && (value.getName().length() - lastIndex == suffix.length())) {
                    treeIterator.remove();
                    continue start;
                }
            }

            // 过滤前缀
            for (String prefix : filterPrefixList) {
                if (value.getName().startsWith(prefix)) {
                    treeIterator.remove();
                    continue start;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "AddConfigUtil{" +
                "filterFiles='" + filterFiles + '\'' +
                ", filterDirectorys='" + filterDirectorys + '\'' +
                ", filterSuffixs='" + filterSuffixs + '\'' +
                ", filterPrefixs='" + filterPrefixs + '\'' +
                ", filterFileList=" + filterFileList +
                ", filterDirectoryList=" + filterDirectoryList +
                ", filterSuffixList=" + filterSuffixList +
                ", filterPrefixList=" + filterPrefixList +
                '}';
    }
}
