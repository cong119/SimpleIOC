package com.cong.ioc.reader;

import com.cong.ioc.annotation.Autowired;
import com.cong.ioc.bean_factory.BeanDefinitionRegistry;
import com.cong.ioc.beans.BeanDefinition;
import com.cong.ioc.beans.BeanReference;
import com.cong.ioc.beans.PropertyValue;
import com.cong.ioc.resource.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.lang.reflect.Field;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    // 将BeanDefinition注册到BeanFactory
    private BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public  void loadBeanDefinitions(String location) throws Exception {
        InputStream is = this.resourceLoader.getResource(location).getInputStream();
        doLoadBeanDefinitions(is);
    }

    protected void doLoadBeanDefinitions(InputStream is) throws Exception {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(is);
            registryBeanDefinitions(document);
        } catch (Exception ex) {
            throw ex;
        } finally {
            is.close();
        }

    }

    protected void registryBeanDefinitions(Document document) {
        Element root = document.getDocumentElement();  //<beans>
        parseBeanDefinitions(root);
    }

    protected void parseBeanDefinitions(Element root) {
        NodeList nodeList = root.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);    // <beans> <bean>
            if(item instanceof Element) {
                Element ele = (Element) item;
                processBeanDefinition(ele);
            }
        }
    }

    protected void processBeanDefinition(Element ele) {
        String name = ele.getAttribute("id");
        String className = ele.getAttribute("class");
        String beanScope = ele.getAttribute("scope");
        String beanLazyInit = ele.getAttribute("lazy-init");

        if(className == null || className.length() == 0) {
            throw new IllegalArgumentException("Configuraton exception: <bean> element must has class attribute");
        }

        if(name == null || name.length() == 0) {
            name = className;
        }

        boolean single = "prototype".equals(beanScope) ? false : true;  //如果设置了多例，则为多例，否则一律单例

        boolean lazyInit = single == true && "false".equals(beanLazyInit) ? false : true;  //多例，延迟加载为true；单例且设置为延迟加载，延迟加载为true

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(className);
        beanDefinition.setSingle(single);
        beanDefinition.setLazyInit(lazyInit);
        processBeanProperty(ele, beanDefinition);  // <beans> <bean> <property>

        processBeanFieldAutowired(beanDefinition);  // <beans> <bean> 处理Autowired注入

        this.registry.registerBeanDefinition(name, beanDefinition);
    }

    protected void processBeanProperty(Element ele, BeanDefinition beanDefinition) {
        NodeList childList = ele.getElementsByTagName("property");

        for(int i = 0; i < childList.getLength(); i++) {
            Node node = childList.item(i);

            if(node instanceof Element) {
                Element property = (Element) node;

                String name = property.getAttribute("name");
                String value = property.getAttribute("value");

                if(value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                } else {
                    String refObj = property.getAttribute("ref");
                    if(refObj == null || refObj.length() == 0) {
                        throw new IllegalArgumentException("Configuraton exception: <property> element must has value or ref attribute");
                    }

                    BeanReference beanReference = new BeanReference(refObj, true);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
                }
            }
        }
    }

    protected void processBeanFieldAutowired(BeanDefinition beanDefinition) {

        try {
            Class clazz = Class.forName(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClass(clazz);

            Field[] fields = clazz.getDeclaredFields();
            if(fields != null && fields.length > 0) {
                for(Field field : fields) {
                    Autowired autowiredAnno = field.getAnnotation(Autowired.class);

                    if(autowiredAnno != null) {
                        String fieldName = field.getName();

                        Class fieldType = field.getType();

                        BeanReference beanReference = new BeanReference(fieldType.getName(), false);

                        beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(fieldName, beanReference));
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Configuraton exception: <bean> element of class, can't be find");
        }
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
