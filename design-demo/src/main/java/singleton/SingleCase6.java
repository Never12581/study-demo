package singleton;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:01 2019/4/14
 */
public class SingleCase6 {

    /**
     * 优点：线程安全，懒加载
     * 缺点：无法解决反序列化带来的问题
     */

    private SingleCase6() {
    }

    public static SingleCase6 getInstance() {
        return SingleCase6Factory.SINGLE_CASE_6;
    }

    private final static class SingleCase6Factory {
        private final static SingleCase6 SINGLE_CASE_6 = new SingleCase6();
    }

}
