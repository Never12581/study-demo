package com.hzx.sentinel;

import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SentinelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelDemoApplication.class, args);

        initSystemRule();

    }

    private static void initSystemRule() {
        List<SystemRule> rules = new ArrayList<SystemRule>();
        SystemRule rule = new SystemRule();
        // max load is 3
        rule.setHighestSystemLoad(3.0);
        // max avg rt of all request is 10 ms
        rule.setAvgRt(10);
        // max total qps is 20
        rule.setQps(20);
        // max parallel working thread is 10
        rule.setMaxThread(10);

        rules.add(rule);
        SystemRuleManager.loadRules(Collections.singletonList(rule));
    }

}

