package com.hzx.monitor.service;

import com.hzx.monitor.annoation.MonitorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 13:59 2019/7/27
 */
@Service
public class TestService {

    private final static Logger logger = LoggerFactory.getLogger(TestService.class);

    @MonitorResource
    public void test() throws Exception {
        logger.info("进方法了");
        if (1 == 1) {
            throw new Exception("哈哈哈");
        }
        System.out.println("出方法了");
    }
}
