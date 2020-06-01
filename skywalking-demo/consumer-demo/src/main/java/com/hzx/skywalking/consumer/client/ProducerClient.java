package com.hzx.skywalking.consumer.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "http://localhost:8001" ,name = "test")
public interface ProducerClient {

    @GetMapping("/producer")
    String producer();
}