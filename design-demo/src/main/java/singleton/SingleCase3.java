package singleton;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:10 2019/4/14
 */
public class SingleCase3 {

    /**
     * 劣势：在个高并发的场景下，获取单例对象，效率不高
     */

    private static SingleCase3 singleCase3 = null;

    private SingleCase3() {
    }

    private synchronized static SingleCase3 getInstance() {
        if (singleCase3 == null) {
            singleCase3 = new SingleCase3();
        }
        return singleCase3;
    }

}
