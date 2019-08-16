package com.xwq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MapperProxy<T> implements InvocationHandler {
    private Class<T> mapperClass;

    public MapperProxy(Class<T> mapperClass) {
        this.mapperClass = mapperClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return MapperHolder.execute(mapperClass, method, args);
    }

    public T getProxy() {
        return (T) Proxy.newProxyInstance(mapperClass.getClassLoader(), new Class[]{mapperClass}, this);
    }
}
