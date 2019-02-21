package com.cong.ioc.bean_factory;

import com.cong.ioc.beans.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
