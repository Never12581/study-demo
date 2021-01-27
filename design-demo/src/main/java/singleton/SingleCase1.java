package singleton;

import java.io.Serializable;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:07 2019/4/14
 */
public class SingleCase1 implements Serializable {

    /**
     * 劣势：在未使用到此单例的情况下，也会初始化对象，造成资源浪费
     */

    private final static SingleCase1 singleCase1 = new SingleCase1();

    private SingleCase1() {
    }

    private static SingleCase1 getInstance() {
        return singleCase1;
    }


    private Object readResolve(){
        return singleCase1;
    }
}
