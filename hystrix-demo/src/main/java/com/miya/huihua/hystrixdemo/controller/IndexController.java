package com.miya.huihua.hystrixdemo.controller;

import com.miya.huihua.hystrixdemo.command.CommandHelloFailure;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:36 2018/11/17
 */
@RestController
public class IndexController {

    /**
     *      spring cloud hystrix 会切换线程池
     *          ThreadLocal
     *              spring 事务
     *
     */

    @GetMapping("")
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(
                    name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "50")

    })
    public String index(@RequestParam(value = "sleep", defaultValue = "10") Integer sleep) throws InterruptedException {
        Thread.sleep(sleep);
        System.out.printf("[%s] index() costs time : %d \n", Thread.currentThread().getName(), sleep);
        return "index";
    }

    public String fallback(Integer sleep) {
        System.out.printf("[%s] index() costs time : %d \n", Thread.currentThread().getName(), sleep);
        return "fallback";
    }

    @GetMapping(value = "hystrix-api")
    public String hystrixApi(@RequestParam(value = "sleep", defaultValue = "10") Integer sleep){

        CommandHelloFailure commandHelloFailure = new CommandHelloFailure("hystrix-api",()->{
            Thread.sleep(sleep);
            System.out.printf("[%s] hystrixApi() costs time : %d \n", Thread.currentThread().getName(), sleep);

            return "hystrix API";
        });

        return commandHelloFailure.execute();
    }

    @GetMapping(value = "demo")
    public String demo() {
        System.out.printf("[%s] Exceution demo \n", Thread.currentThread().getName());
        if(1==1) {
            throw new RuntimeException("哈哈哈");
        }
        return "demo";
    }

}
