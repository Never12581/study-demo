package com.hzx.monitor;

import com.hzx.monitor.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorApplicationTests {

    @Resource
    private TestService testService ;

    @Test
    public void contextLoads() throws Exception {
        testService.test();
    }

}
