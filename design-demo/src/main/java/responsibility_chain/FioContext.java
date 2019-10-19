package responsibility_chain;

/**
 * @author: bocai.huang
 * @create: 2019-10-09 16:04
 **/
public class FioContext {

    /**
     * 用于存储之后传输的数据
     * 如果是 内网到外网 就是 I2OData
     * 如果是 外网到内网 就是 O2IData
     */
    private final static ThreadLocal<FioData> data = new ThreadLocal<>();
    /**
     * 数据类型 type 即为 mesgType
     */
    private final static ThreadLocal<String> type = new ThreadLocal<>();


    public static void setData(FioData fioData) {
        data.set(fioData);
    }

    public static FioData getData() {
        return data.get();
    }

    public static void setType(String mesgType) {
        type.set(mesgType);
    }

    public static String getType() {
        return type.get();
    }

    public static void remove() {
        data.remove();
        type.remove();
    }

}
