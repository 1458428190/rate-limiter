package org.gucha.ratelimiter.core.framework.extension;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 上午10:28
 */
public class ExtensionLoader {
    private static final ConcurrentHashMap<Class<?>, Object> CLASS_LOCKS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, List<?>> SINGLETON_OBJECTS = new ConcurrentHashMap<>();

    public static <T> T getExtension(Class<T> clazz) {
        return getExtension(clazz, true);
    }

    public static <T> T getExtension(Class<T> clazz, boolean isSingleton) {
        List<T> extensionList = getExtensionList(clazz);
        if (CollectionUtils.isEmpty(extensionList)) {
            return null;
        }
        return extensionList.get(0);
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        return getExtensionList(clazz, true);
    }

    public static <T> List<T> getExtensionList(Class<T> clazz, boolean isSingleton) {
        if (isSingleton) {
            List<T> extensions = (List<T>) SINGLETON_OBJECTS.get(clazz);
            if (CollectionUtils.isNotEmpty(extensions)) {
                return extensions;
            }
        }
        synchronized (getLoadClassLock(clazz)) {
            // 非单例,返回所有
            if (!isSingleton) {
                return load(clazz);
            }
            // 单例
            if(SINGLETON_OBJECTS.containsKey(clazz)) {
                return (List<T>) SINGLETON_OBJECTS.get(clazz);
            }
            // 其他
            List<T> serviceList = load(clazz);
            if(CollectionUtils.isNotEmpty(serviceList)) {
                SINGLETON_OBJECTS.put(clazz, serviceList);
            }
            return serviceList;
        }
    }

    private static <T> List<T> load(Class<T> clazz) {
        List<T> serviceList = new ArrayList<>();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        for (T service : serviceLoader) {
            serviceList.add(service);
        }
        if (!serviceList.isEmpty()) {
            Collections.sort(serviceList, OrderComparator.INSTANCE);
        }
        return serviceList;
    }

    private static <T> Object getLoadClassLock(Class<T> clazz) {
        Object lock = CLASS_LOCKS.get(clazz);
        if (lock == null) {
            Object newLock = new Object();
            lock = CLASS_LOCKS.putIfAbsent(clazz, newLock);
            if (lock == null) {
                lock = newLock;
            }
        }
        return lock;
    }
}
