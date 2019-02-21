package com.cong.ioc.beans;

public class BeanDefinition {

    // bean的名称
    private String beanName;

    // bean的Class对象
    private Class beanClass;

    // bean的全限定类名
    private String beanClassName;

    // 单例 or 多例
    private boolean single;

    // 饿汉 or 懒汉
    private boolean lazyInit;

    // bean所依赖的属性
    private PropertyValues propertyValues = new PropertyValues();

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
