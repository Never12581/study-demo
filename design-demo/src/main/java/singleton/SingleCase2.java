package singleton;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:08 2019/4/14
 */
public class SingleCase2 {

    /**
     * 劣势：在高并非的情况下，容易生成多个实例，造成资源浪费
     */

    private static SingleCase2 singleCase2 = null;

    private SingleCase2() {
    }

    private static SingleCase2 getInstance() {
        if (singleCase2 == null) {
            singleCase2 = new SingleCase2();
        }
        return singleCase2;
    }

}
