package com.cong.ioc.bean_factory;

import com.cong.ioc.beans.BeanDefinition;
import com.cong.ioc.beans.BeanReference;
import com.cong.ioc.beans.PropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    private Map<String, Object> singleBeans = new ConcurrentHashMap<>();

    protected void addSingleBean(String beanName, Object bean) {
        this.singleBeans.put(beanName, bean);
    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if(beanDefinition == null) {
            throw new IllegalArgumentException("Bean for name '" + beanName + "' can't find Definition");
        }

        Object returnBean = null;

        if(beanDefinition.isSingle()) {
            returnBean = singleBeans.get(beanName);

            if(returnBean != null) {
                return returnBean;
            } else {
                if(!beanDefinition.isLazyInit()) {
                    System.out.println("Can't find single bean '" + beanName + "', will crate a new bean");
                }
            }
        }

        if(returnBean == null) {
            returnBean = doCreateBean(beanDefinition);
        }

        if(beanDefinition.isSingle()) {
            singleBeans.put(beanName, returnBean);
        }
        return returnBean;
    }

    public Object getBean(String beanClass, boolean isInstance) {
        BeanDefinition beanDefinition = getBeanDefinitionByClass(beanClass);
        if(beanDefinition == null) {
            throw new IllegalArgumentException("Bean for type '" + beanClass + "' can't find Definition");
        }

        Object returnBean = null;

        if(beanDefinition.isSingle()) {
            returnBean = this.getBeanByClass(beanClass);

            if(returnBean != null) {
                return returnBean;
            } else {
                if(!beanDefinition.isLazyInit()) {
                    System.out.println("Can't find single bean '" + beanClass + "', will crate a new bean");
                }
            }
        }

        if(returnBean == null) {
            returnBean = doCreateBean(beanDefinition);
        }

        if(beanDefinition.isSingle()) {
            String beanName = beanClass.substring(beanClass.lastIndexOf('.') + 1);
            beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
            singleBeans.put(beanName, returnBean);
        }
        return returnBean;
    }

    protected Object getBeanByClass(String beanClass) {
        for(Map.Entry<String, Object> beanEntry : singleBeans.entrySet()) {
            Object beanObj = beanEntry.getValue();

            if(beanObj.getClass().getName().equals(beanClass)) {
                return beanObj;
            }
        }
        return null;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract BeanDefinition getBeanDefinitionByClass(String beanName);

    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Object bean = createInstance(beanDefinition);
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    protected Object createInstance(BeanDefinition beanDefinition) {
        try {
            if(beanDefinition.getBeanClass() != null) {
                return beanDefinition.getBeanClass().newInstance();
            } else if(beanDefinition.getBeanClassName() != null) {
                try {
                    Class clazz = Class.forName(beanDefinition.getBeanClassName());
                    beanDefinition.setBeanClass(clazz);
                    return clazz.newInstance();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException("Bean Class '" + beanDefinition.getBeanClassName() + "' cannot be find");
                }
            } else {
                throw new RuntimeException("Bean '" + beanDefinition.getBeanName() + "' has not Class");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Create bean '" + beanDefinition.getBeanName() + "' faild");
        }
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) {
        for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();

            if(value instanceof BeanReference) {
                BeanReference reference = (BeanReference) value;

                if(reference.isInstance()) {
                    value = getBean(reference.getRef());
                } else {
                    value = getBean(reference.getRef(), false);
                }
            }

            try {
                String setMethod = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = bean.getClass().getDeclaredMethod(setMethod, value.getClass());
                method.setAccessible(true);
                method.invoke(bean, value);
            } catch (NoSuchMethodException ex) {
                System.out.println("Bean '" + bean.getClass() + "' has not set method for '" + name + "'");
                try {
                    Field field = bean.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (NoSuchFieldException nsex) {
                    throw new RuntimeException("Bean '" + bean.getClass() + "' has not field of '" + name + "'");
                } catch (IllegalAccessException iaex) {
                    throw new RuntimeException("Bean '" + bean.getClass() + "' inject field '" + name + "' faild");
                }
            } catch (InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException("Bean '" + bean.getClass() + "' inject field '" + name + "' faild");
            }
        }
    }

    public abstract void preInstantiateSingletons() throws Exception;
}
