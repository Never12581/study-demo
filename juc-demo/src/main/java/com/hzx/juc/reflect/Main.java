package com.hzx.juc.reflect;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;

/**
 * @author: bocai.huang
 * @create: 2019-10-11 11:12
 **/
public class Main {

    public static void main(String[] args)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        I2ODataDetail i2ODataDetail = new I2ODataDetail();
        i2ODataDetail.setName("hbc");
        System.out.println(JSON.toJSONString(i2ODataDetail));

        DataContext.setI2oData(i2ODataDetail);

        I2OData i2OData = DataContext.getI2oData();

        String className = "com.hzx.juc.reflect.I2ODataDetail";

        Class clazz = Class.forName(className);

        Object object = i2OData;

        Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);
        field.set(i2OData,"哈哈哈");
        field.setAccessible(false);

        System.out.println(JSON.toJSONString(i2OData));

    }

}
