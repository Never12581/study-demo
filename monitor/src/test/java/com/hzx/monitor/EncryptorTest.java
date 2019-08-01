package com.hzx.monitor;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:09 2019/8/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EncryptorTest {

    @Resource
    StringEncryptor stringEncryptor;

    @Test
    public void getPass(){
        String name = stringEncryptor.encrypt("Huang-04237");
        System.out.println(name);
    }

}
