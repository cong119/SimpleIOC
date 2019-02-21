package com.cong.ioc.resource;

// 简单工厂模式
// 资源加载，可以根据不同的资源类型：如URL，PC目录等，返回不同的Resource的实现类
public class ResourceLoader {

    final String CLASSPATH_URL_PREFIX = "classpath:";

    final String INTERNET_URL_PREFIX = "http://";

    public Resource getResource(String location) {
        if(location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClasspathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else if(location.startsWith(INTERNET_URL_PREFIX)) {
            return new InternetResource(location);
        } else {
            return new ClasspathResource(location);
        }
    }

}
