package com.qk.log.util;

import java.util.Collection;

/**
 * @description:集合工具类
 * @author: anzhonghui
 * @date: 2019年3月29日下午12:47:32
 * @modify:
 */
public class CollectionUtils {

    /**
     * @description:判断集合是否为空
     * @author: anzhonghui
     * @date: 2019年3月29日下午12:47:32
     * @modify:
     */
    public static boolean isNull(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * @description:判断集合不为空 虽然上面那个方法加个非就与该方法一样，但觉得非是多余的操作
     * @author: anzhonghui
     * @date: 2019年3月29日下午12:47:32
     * @modify:
     */
    public static boolean isNotNull(Collection<?> coll) {
        return (coll != null && coll.size() > 0);
    }
}
