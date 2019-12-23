package com.hzx.juc.reflect;

/**
 * @author: bocai.huang
 * @create: 2019-10-11 11:08
 **/
public class I2ODataDetail implements I2OData {

    private String name;

    public String getName() {
        return name;
    }

    public I2ODataDetail setName(String name) {
        this.name = name;
        return this;
    }
}
