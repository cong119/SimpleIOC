package com.cong.ioc.beans;

public class BeanReference {

    private String ref;

    private boolean isInstance;

    public BeanReference(String ref, boolean isInstance) {
        this.ref = ref;
        this.isInstance = isInstance;
    }

    public String getRef() {
        return ref;
    }

    public boolean isInstance() {
        return isInstance;
    }
}
