package com.weitaomi.systemconfig.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 集合分组工具类
 * Created by Martin on 2015/6/26.
 */
public class GroupUtils {

    private static final Logger logger = LoggerFactory.getLogger(GroupUtils.class);

    /**
     * 分组依据接口，用于集合分组时，获取分组依据
     * @param <T>
     */
    public interface GroupBy<T> {
        T groupby(Object obj);
    }

    /**
     * 根据分组依据对集合进行分组
     * @param colls
     * @param gb
     * @param <T>
     * @param <D>
     * @return
     */
    public static final <T extends Comparable<T> ,D> Map<T ,List<D>> group(Collection<D> colls ,GroupBy<T> gb){
        if(colls == null || colls.isEmpty()) {
            logger.info("分组集合不能为空");
            return null;
        }
        if(gb == null) {
            logger.info("分组依据接口不能为NULL");
            return null;
        }
        Iterator<D> iter = colls.iterator();
        Map<T ,List<D>> map = new HashMap<T, List<D>>();
        while(iter.hasNext()) {
            D d = iter.next();
            T t = gb.groupby(d);
            if(map.containsKey(t)) {
                map.get(t).add(d);
            } else {
                List<D> list = new ArrayList<D>();
                list.add(d);
                map.put(t, list);
            }
        }
        return map;
    }

}
