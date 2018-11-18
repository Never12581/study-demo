package com.miya.huihua.hystrixdemo.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.concurrent.Callable;

public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    private final Callable<String> runnable;

    public CommandHelloFailure(String name , Callable runnable) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup-10086"),50);
        this.name = name;
        this.runnable = runnable;
    }

    @Override
    protected String run() {
        try {
            return runnable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String getFallback() {
        System.out.printf("[%s] CommandHelloFailure.getFallback demo \n", Thread.currentThread().getName());
        return "Hello Failure " + name + "!";
    }
}