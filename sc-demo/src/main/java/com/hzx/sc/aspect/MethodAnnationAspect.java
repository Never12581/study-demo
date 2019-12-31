package com.hzx.sc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: bocai.huang
 * @create: 2019-10-11 09:49
 **/
@Component
@Aspect
public class MethodAnnationAspect {

    @Pointcut(value = "@annotation(com.hzx.sc.annation.MethodAnnation)")
    public void test(){}

    @Around(value = "test()")
    public Object around(ProceedingJoinPoint joinpoint){
        try {
            System.out.println("我到切面了");
            return joinpoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
