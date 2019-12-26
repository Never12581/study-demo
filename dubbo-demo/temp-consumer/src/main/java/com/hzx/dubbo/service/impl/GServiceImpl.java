package com.hzx.dubbo.service.impl;

import com.hzx.dubbo.service.GService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:20 2019/12/25
 */
@Slf4j
@Service
public class GServiceImpl implements GService {

    @Override
    public String test4G(String gtest) {
        log.info("【接收到的参数:】{}", gtest);
        return "【返回参数】：" + gtest;
    }

}
