package com.cong.ioc.bean_factory;

import com.cong.ioc.beans.BeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    private Map<String, BeanDefinition> beanDefinitionClassMap = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    protected BeanDefinition getBeanDefinitionByClass(String beanClass) {
        for(Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();

            if(beanDefinition.getBeanClassName().equals(beanClass)) {
                return beanDefinition;
            }
        }
        return null;
    }

    @Override
    public void preInstanceSingletons() throws Exception {
        for(String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            if(beanDefinition.isSingle() && !beanDefinition.isLazyInit()) {
                Object bean = doCreateBean(beanDefinition);
                addSingleBean(beanName, bean);
            }
        }
    }

}
