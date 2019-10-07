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
    // 是否启用过滤文件
    @Value("${no-permission.enable}")
    private Boolean noPermissionEnable;
    // 是否启用过滤文件
    @Value("${specified-path.enable}")
    private Boolean specifiedPathEnable;
    @Value("${specified-path.directory}")
    private String specifiedPathDirectory;

    private List<String> filterFileList = new ArrayList<>();
    private List<String> filterDirectoryList = new ArrayList<>();
    private List<String> filterSuffixList = new ArrayList<>();
    private List<String> filterPrefixList = new ArrayList<>();
    private List<String> specifiedPathList = new ArrayList<>();

    // 配置文件的路径
    public static String addConfigPath = "";
    // 加载外部配置文件的路径
    static {
        addConfigPath = System.getProperty("user.dir") + File.separator + "show-log" + File.separator
                + "add-config.yml";
    }

    /**
     * @param
     * @Description: 初始化过滤文件
     * @return:
     * @Author: huihui
     * @CreateDate: 2019/9/7 15:41
     */
    public void init() {
        // 过滤文件初始化
        if (noPermissionEnable) {
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

        // 指定路径初始化
        if (specifiedPathEnable) {
            if (CollectionUtils.isNull(specifiedPathList) && StringUtils.isNotEmpty(specifiedPathDirectory)) {
                specifiedPathList = Arrays.asList(specifiedPathDirectory.split(","));
            }
        }
    }

    /**
     * @Description: 判断指定的初始化满足
     * @Author: huihui
     * @CreateDate: 2019/10/1 19:00
     */
    public Boolean judgeSpecifiedPath(String filePath){
        // 如果不启用指定路径；判断指定的路径是否有内容
        if (!specifiedPathEnable || StringUtils.isEmpty(specifiedPathDirectory)) {
            return true;
        }

        // 初始化指定的文件
        init();

        // 判读传入路径是否符合要求
        for (String specifiedPath : specifiedPathList) {
            if (filePath.startsWith(specifiedPath)) {
                return true;
            }
        }

        // 最终都不符合返回false
        return false;
    }

    /**
     * @Description: 指定路径，指定可以查看的目录
     * @Author: huihui
     * @CreateDate: 2019/10/1 18:51
     */
    public void specifiedPath(Map<Integer, Tree> treeMap){
        // 如果不启用指定路径
        if (!specifiedPathEnable || StringUtils.isEmpty(specifiedPathDirectory)) {
            return;
        }

        // 遍历树，使用迭代器的方法，因为要删除元素
        Iterator<Map.Entry<Integer, Tree>> treeIterator = treeMap.entrySet().iterator();
        start:
        while (treeIterator.hasNext()) {
            Map.Entry<Integer, Tree> entry = treeIterator.next();
            Tree value = entry.getValue();
            // 如果符合指定的路径，继续
            for (String specifiedPath : specifiedPathList) {
                if (value.getPath().startsWith(specifiedPath)) {
                    continue start;
                }
            }
            // 走到这里说明不符合，移除元素
            treeIterator.remove();
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
        // 如果没有启用，不过滤
        if (!noPermissionEnable) {
            return;
        }

        // 初始化过滤的内容，这样可以保证获取的过滤内容是最新的
        init();
        // 遍历树，使用迭代器的方法，因为要删除元素
        Iterator<Map.Entry<Integer, Tree>> treeIterator = treeMap.entrySet().iterator();
        start:
        while (treeIterator.hasNext()) {
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
