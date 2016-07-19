package com.weitaomi.systemconfig.util;

import java.util.Collection;
import java.util.List;

/**
 * 速普集合工具类
 * Created by Jemry on 2015/11/21.
 */
public class SpCollectionUtils {

    public static final Collection NULL_COLLECTION = new NullCollection();

    /**
     * 返回一个只包含null值的一个集合,用于去除其他集合中的null值
     * @param <T>
     * @return
     */
    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }
}
