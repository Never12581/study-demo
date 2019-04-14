package singleton;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:24 2019/4/14
 */
public enum SingleCase5 {

    /**
     * 优点：
     * （1）普通的Java类的反序列化过程中，会通过反射调用类的默认构造函数来初始化对象
     * （2）在序列化的时候Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
     * 同时，编译器是不允许任何对这种序列化机制的定制的，因此禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法。
     * 
     * 缺点：同饿汉模式一致，在JVM初始化的时候，就加载了，如果未曾用到该单例，比较浪费资源
     */

    INSTANCE;

    // other methods

}
