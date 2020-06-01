package com.hzx.skywalking.consumer.controller;

import com.hzx.skywalking.consumer.client.ProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    private ProducerClient producerClient;

    @GetMapping
    public String consumer() {
        log.info("consumer something");
        // 通过feign调用
        String result = producerClient.producer();
        return "consumer: " + result;
    }
}