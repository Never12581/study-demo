package com.hzx.skywalking.producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @GetMapping
    public String producer() {
        log.info("received a request");
        return "this message from producer";
    }
}