package com.cong.ioc.context;

import com.cong.ioc.bean_factory.BeanDefinitionRegistry;
import com.cong.ioc.reader.XmlBeanDefinitionReader;
import com.cong.ioc.resource.ResourceLoader;

public class ClasspathXmlApplicationContext extends AbstractApplicationContext {

    private String location;

    @Override
    protected void loadBeanDefinitions(BeanDefinitionRegistry registry) throws Exception {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry, new ResourceLoader());
        reader.loadBeanDefinitions(location);
    }

    public ClasspathXmlApplicationContext(String location) {
        this.location = location;
        try {
            refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
