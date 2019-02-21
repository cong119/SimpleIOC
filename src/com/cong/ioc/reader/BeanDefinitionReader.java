package com.cong.ioc.reader;

public interface BeanDefinitionReader {
    void loadBeanDefinitions(String location) throws Exception;
}
