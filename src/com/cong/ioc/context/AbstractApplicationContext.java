package com.cong.ioc.context;

import com.cong.ioc.bean_factory.BeanDefinitionRegistry;
import com.cong.ioc.bean_factory.DefaultBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory beanFactory;

    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

    public void refresh() throws Exception {
        this.beanFactory = new DefaultBeanFactory();
        loadBeanDefinitions(beanFactory);
        beanFactory.preInstanceSingletons();
    }

    protected abstract void loadBeanDefinitions(BeanDefinitionRegistry registry) throws Exception;
}
