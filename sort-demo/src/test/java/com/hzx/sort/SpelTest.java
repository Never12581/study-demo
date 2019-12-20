package com.hzx.sort;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author: bocai.huang
 * @create: 2019-11-11 14:57
 **/
public class SpelTest {

    /**
     * 字符串拼接
     */
    @Test
    public void test1(){
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue(); // "hello world"

        exp = parser.parseExpression("'Hello World'.concat('!')");
        message = (String) exp.getValue();// "Hello World!"
        System.out.println(message);
    }

    /**
     * 字符串截取
     */
    @Test
    public void test2(){
        String expresion = "${0}.substring(3,4)";

        expresion = expresion.replace("${0}","'abcdefg'") ;
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expresion);
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    /**
     * 数组求和
     */
    @Test
    public void test3(){
        String expression = "'abcdefg'.length()>5?'hh':'ee'";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        System.out.println(exp.getValue());
    }

    @Test
    public void test4() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    }

}