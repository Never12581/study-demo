package com.hzx.sc.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:12 2019/4/2
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private LocalClient localClient;

    @Override
    public String test(String hhh, String ooo) {
        return localClient.test(hhh, ooo);
    }

    /**
     * @Author: bocai.huang
     * @Descripition:
     * @Date: Create in 17:30 2019/3/21
     */
    @FeignClient(value = "${ali.coupon.server.name}", url = "${ali.coupon.server.url}")
    private interface LocalClient {

        @GetMapping(value = "test")
        String test(@RequestHeader(value = "hhh") String hhh, @RequestParam(value = "ooo") String ooo);

    }
}
