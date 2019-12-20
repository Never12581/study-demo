package com.hzx.sort;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

/**
 * @author: bocai.huang
 * @create: 2019-11-11 13:41
 **/
public class MvelTest {

    @Test
    public void testStringSub() {
        ParserContext ctx = new ParserContext();
        ctx.setStrongTyping(true);
        ctx.addInput("str", String.class);
        ctx.addIndexedInput("FDSLKFJDLSJ");
        String expression = "str.subString(3,4)";
        Serializable ce = MVEL.compileExpression(expression, ctx);
        System.out.println(ce);
    }

    @Test
    public void testStringSubMap(){
        String s = "str.subString(3,4)";
        Map<String,Object> map = new HashMap<>();
        map.put("str","abcdefg");

        String result = (String) MVEL.eval(s,map);
        System.out.println(result);
    }

    @Test
    public void testStringBlank() {
        String s = "abs";
        String condition = "(s ==null) || (s.length == 0) ";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("s", s);
        System.out.println(MVEL.evalToBoolean(condition, paramsMap));
    }

    @Test
    public void testMethod() {
        String s ="a = 10;\n"
            + "b = (a = a * 2) + 10;\n"
            + "a;";

        s = MVEL.evalToString(s);
        System.out.println(s);
    }

}
