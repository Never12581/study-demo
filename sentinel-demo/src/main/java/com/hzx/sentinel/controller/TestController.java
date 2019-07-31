package com.hzx.sentinel.controller;

import com.hzx.sentinel.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:28 2019/1/22
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping(value = "/test")
    public String test() {
        String res = testService.test();
        return res ;
    }

}
