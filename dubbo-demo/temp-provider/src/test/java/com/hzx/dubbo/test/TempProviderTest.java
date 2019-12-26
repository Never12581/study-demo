package com.hzx.dubbo.test;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:24 2019/12/25
 */
// @RunWith(SpringRunner.class)
// @SpringBootTest
public class TempProviderTest {

    @Test
    public void test() {
        ReferenceConfig<GenericService> ref = new ReferenceConfig<>();
        ApplicationConfig appConfig = new ApplicationConfig("temp-provider");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("39.98.143.126:2181");

        ref.setProtocol("dubbo");
        ref.setApplication(appConfig);
        ref.setRegistry(registryConfig);

        ref.setInterface("com.hzx.dubbo.service.GService");
        ref.setGeneric(true);

        GenericService gs = ref.get();
        Object result = gs.$invoke("test4G",new String[]{"java.lang.String"},new Object[]{"哈哈哈"});

        System.out.println(result);
    }

}
