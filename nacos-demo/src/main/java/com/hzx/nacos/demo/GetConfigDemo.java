package com.hzx.nacos.demo;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.PropertiesListener;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: bocai.huang
 * @create: 2020-01-28 23:31
 **/
public class GetConfigDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetConfigDemo.class);
    public static void main(String[] args) throws NacosException {
        Properties properties = new Properties();
        // 指定配置的 DataID 和 Group
        String dataId = "com.hzx.file.server";
        String group = "DEFAULT_GROUP";
        String content = "connectTimeoutInMills=5000";
        // 从控制台命名空间管理的"命名空间详情"中拷贝 endpoint、namespace
        properties.put(PropertyKeyConst.ENDPOINT, "39.98.143.126");
        properties.put(PropertyKeyConst.NAMESPACE, "3aad262d-25b2-4a1c-a44f-1b5cd2625505");
        // 推荐使用 RAM 账号的 accessKey、secretKey，
//        properties.put(PropertyKeyConst.ACCESS_KEY, "${accessKey}");
//        properties.put(PropertyKeyConst.SECRET_KEY, "${secretKey}");
        ConfigService configService = NacosFactory.createConfigService(properties);
        String config = configService.getConfig(dataId, group, 5000);
        LOGGER.info("getConfig: {}", config);
    }

}
