package com.hzx.juc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * @author: bocai.huang
 * @create: 2019-08-30 16:28
 **/
public class NotImportTest {

    @Test
    public void test(){
       List<String> should = Arrays.asList(new String[]{
           "1000000032792",
           "1000000034585",
           "1000000072171",
           "1000000082215",
           "1000000103685",
           "1000000115386",
           "1000000165259",
           "1000000178522",
           "1000000204375",
           "1000000204653",
           "1000000208193",
           "1000000209285",
           "1000000213153",
           "1000000213652",
           "1000000215212",
           "1000000358272",
           "1000000194084",
           "1000000194084",
           "1000000194084",
           "1000000194084",
           "1000000194084",
           "1000000358259",
           "1000000358260",
           "1000000358258",
           "1000000326999",
           "1000000324641",
           "1000000343369",
           "1000000326697",
           "1000000358271",
           "1000000326624",
           "1000000357783",
           "1000000358270"
       });

       List<String> actual = Arrays.asList(new String[]{
           "1000000032792",
           "1000000034585",
           "1000000072171",
           "1000000082215",
           "1000000103685",
           "1000000115386",
           "1000000165259",
           "1000000204653",
           "1000000208193",
           "1000000209285",
           "1000000213153",
           "1000000213652",
           "1000000215212",
           "1000000326697",
           "1000000343369",
           "1000000326999",
           "1000000357783",
           "1000000324641",
           "1000000326624"
       });

        System.out.println("should List:"+should.size());
        System.out.println("actual List:"+actual.size());

        Set<String> shouldSet = new HashSet<>();
        shouldSet.addAll(should);
        System.out.println("should set:"+shouldSet.size());
        Set<String> actualSet = new HashSet<>();
        actualSet.addAll(actual);
        System.out.println("actual set:"+actualSet.size());

        /**
         * 现在我们要求出，存在于shouldset却不存在于actualSet中的数据
         */

        for(String s : shouldSet) {
            boolean flag = actualSet.contains(s);
            if(!flag) {
                System.out.println(s);
            }
        }

    }

}
