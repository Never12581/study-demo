package com.hzx.dubbo.service.impl;

import com.hzx.dubbo.service.GService;

/**
 * @author: bocai.huang
 * @create: 2019-12-30 14:51
 **/
public class T1GserviceImpl implements GService {

    @Override
    public String test4G(String gtest) {
        return "【T1 实现】" + gtest;
    }
}
