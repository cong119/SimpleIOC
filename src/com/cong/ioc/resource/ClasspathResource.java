package com.cong.ioc.resource;

import java.io.IOException;
import java.io.InputStream;

// 读取classpath下的资源文件
public class ClasspathResource implements Resource {

    private String path;

    private ClassLoader classLoader;

    public ClasspathResource(String path) {
        this(path, null);
    }

    public ClasspathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return classLoader.getResourceAsStream(path);
    }

}
