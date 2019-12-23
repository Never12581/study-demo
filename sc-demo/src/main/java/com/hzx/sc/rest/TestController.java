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
import org.springframework.beans.factory.annotation.Autowired;
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


}
