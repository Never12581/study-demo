package com.hzx.sc.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: bocai.huang
 * @create: 2019-09-19 14:39
 **/
public class TestTimeTask {

    final Map<String,String> map = new HashMap<>();
    final Random random = new Random();

//    @Scheduled(cron = "0/1 * * * * ?")
    public void execuete(){
        for(int i = 0 ; i < 10001 ; i++ ) {
            map.put(i+" "+ random.nextInt(),"a"+i);
        }
        System.out.println(map.size());
        if(map.size() >= 100000) {
            map.clear();
        }
    }

}
