package com.hzx.dubbo.spi;

import com.hzx.dubbo.service.GService;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author: bocai.huang
 * @create: 2019-12-30 14:56
 **/
public class TestJavaSPI {

    public static void main(String[] args) {
        ServiceLoader<GService> s = ServiceLoader.load(GService.class);
        Iterator<GService> iterator = s.iterator();
        while (iterator.hasNext()) {
            GService search = iterator.next();
            System.out.println(search.test4G("哈哈哈"));
        }


    }

}
