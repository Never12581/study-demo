package com.hzx.dubbo.controller;

import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:54 2019/12/25
 */
@RestController
@RequestMapping(value = "/")
public class TempProviderController {

    private Map<String, GenericService> gsMap = new HashMap<>();

    @GetMapping
    public String testG() {

        // method one
        GenericService gs = getGenericService("com.hzx.dubbo.service.GService");
        Object result = gs.$invoke("test4G", new String[] { "java.lang.String" }, new Object[] { "哈哈哈" });
        System.out.println(result);

        // method two
        gs = getGenericService("com.hzx.dubbo.service.UserProvider");
        Object k = gs.$invoke("getUser", new String[] { "java.lang.String" }, new Object[] { "acb" });
        System.out.println(JSON.toJSONString(k));

        return (String) result;
    }

    private GenericService getGenericService(String interfaceName) {
        if (gsMap.get(interfaceName) != null) {
            return gsMap.get(interfaceName);
        }
        ReferenceConfig<GenericService> ref = new ReferenceConfig<>();
        ApplicationConfig appConfig = new ApplicationConfig("temp-consumer");

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress("39.98.143.126:2181");

        ref.setProtocol("dubbo");
        ref.setApplication(appConfig);
        ref.setRegistry(registryConfig);

        ref.setInterface(interfaceName);
        ref.setGeneric(true);
        GenericService gs = ref.get();
        gsMap.putIfAbsent(interfaceName, gs);
        return gs;
    }

}
