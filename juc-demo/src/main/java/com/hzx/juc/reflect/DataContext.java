package com.hzx.juc.reflect;

/**
 * @author: bocai.huang
 * @create: 2019-10-11 11:09
 **/
public class DataContext {

    private static final ThreadLocal<I2OData> i2oData = new ThreadLocal<>();

    public static void setI2oData(I2OData data) {
        i2oData.set(data);
    }

    public static I2OData getI2oData(){
        return i2oData.get();
    }

    public static void remove(){
        i2oData.remove();
    }

}
