package com.hzx.sc.rest;

import com.hzx.sc.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:34 2019/3/21
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping(value = "test")
    public String test(@RequestHeader(value = "hhh") String hhh, @RequestParam(value = "ooo") String ooo) {
        System.out.println("hhh:" + hhh + " ooo:" + ooo);
        return hhh + ooo;
    }

    @GetMapping(value = "test2")
    public String test2() {
        return testService.test("h1h1", "o1o1");
    }

}
