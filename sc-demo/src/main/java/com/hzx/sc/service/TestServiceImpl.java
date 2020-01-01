package com.hzx.sc.service;

import com.hzx.sc.annation.MethodAnnation;
import org.springframework.stereotype.Service;

/**
 * @author: bocai.huang
 * @create: 2019-10-11 09:47
 **/
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test(String hhh, String ooo) {
        test();
        return "AAA";
    }

    @MethodAnnation
    private void test() {
        System.out.println("能够捕获到吗？");
    }
}
