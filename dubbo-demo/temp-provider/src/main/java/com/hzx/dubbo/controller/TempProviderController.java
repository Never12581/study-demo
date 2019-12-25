package com.hzx.dubbo.controller;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:54 2019/12/25
 */
@RestController
@RequestMapping(value = "/")
public class TempProviderController {

    @GetMapping
    public String testG(){
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
        return (String) result;
    }

}
