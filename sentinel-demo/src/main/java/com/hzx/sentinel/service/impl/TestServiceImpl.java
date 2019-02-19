package com.hzx.sentinel.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.hzx.sentinel.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:26 2019/1/22
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    @SentinelResource(value = TEST)
    public String test() {
        String str = "test";
        System.out.println(str);
        return str;
    }
}
