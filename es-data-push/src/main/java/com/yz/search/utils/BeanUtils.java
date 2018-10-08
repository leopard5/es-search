package com.yz.search.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author yazhong.qi
 * @date 2018/7/2.
 */
public class BeanUtils {
    /**
     * 将对象装换为map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
}
