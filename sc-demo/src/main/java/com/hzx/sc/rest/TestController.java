package com.hzx.sc.rest;

import com.hzx.sc.config.ApplicationResource;
import com.hzx.sc.service.TestService;
import com.hzx.sort.bucket.BucketSort;
import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.merge.MergeSort;
import com.hzx.sort.quick.QuickSort;
import com.hzx.sort.radix.RadixSort;
import com.hzx.sort.shell.ShellSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.hzx.sort.BaseSort.randomArray;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:34 2019/3/21
 */
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    // @Resource
    // private TestService testService;
    //
    // @GetMapping(value = "test")
    // public String test(@RequestHeader(value = "hhh") String hhh, @RequestParam(value = "ooo") String ooo) {
    //     System.out.println("hhh:" + hhh + " ooo:" + ooo);
    //     return hhh + ooo;
    // }
    //
    // @GetMapping(value = "test2")
    // public String test2() {
    //     return testService.test("h1h1", "o1o1");
    // }
    //
    // @GetMapping(value = "test3")
    // public void test3() {
    //     for (int i = 0; i < Integer.MAX_VALUE; i++) {
    //         ApplicationResource.RESOURCE.execute(() -> {
    //             System.out.println("==============Heap=============");
    //             int[] array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             long start = System.currentTimeMillis();
    //             HeapSort.sortRoot(array);
    //             System.out.println("Heap sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //
    //             System.out.println("==============Shell=============");
    //             array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             start = System.currentTimeMillis();
    //             ShellSort.sortRoot(array);
    //             System.out.println("Shell sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //
    //
    //             System.out.println("==============Quick=============");
    //             array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             start = System.currentTimeMillis();
    //             QuickSort.sortRoot(array, 0, array.length - 1);
    //             System.out.println("Quick sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //
    //             System.out.println("==============Merge=============");
    //             array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             start = System.currentTimeMillis();
    //             MergeSort.sortRoot(array, 0, array.length - 1);
    //             System.out.println("Merge sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //
    //             System.out.println("==============Bucket=============");
    //             array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             start = System.currentTimeMillis();
    //             BucketSort.sortRoot(array);
    //             System.out.println("Bucket sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //
    //             System.out.println("==============Radix=============");
    //             array = randomArray();
    //             System.out.println("before sortRoot : ");
    //             start = System.currentTimeMillis();
    //             RadixSort.lsdSort(array);
    //             System.out.println("Radix sortRoot cost time : " + (System.currentTimeMillis() - start));
    //             System.out.println("after sortRoot : ");
    //         });
    //     }
    // }
    //
    // @GetMapping(value = "test4")
    // public void test4(){
    //     // int[] array = randomArray();
    //     // System.out.println("before sortRoot : ");
    //     // long start = System.currentTimeMillis();
    //     // MergeSort.sortRoot(array, 0, array.length - 1);
    //     // System.out.println("Merge sortRoot cost time : " + (System.currentTimeMillis() - start));
    //     // System.out.println("after sortRoot : ");
    //
    //     stack();
    //
    // }
    //
    // private synchronized String stack(){
    //     System.out.println(stack());
    //     return "hhhh";
    // }

}
