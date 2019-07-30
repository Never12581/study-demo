package template;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: bocai.huang
 * @create: 2019-05-20 11:24
 **/
public class CommonDataSynchronizationImpl<E,T> extends AbstractDataSynchronization<E, T> {

    @Override
    protected List<T> dataTrans(List<E> eList) {
        List<T> tList = new ArrayList<>(eList.size());
        for(E e : eList) {
            Object o = new Object();
            T t = (T) o ;
            // 数据拷贝 ~
            tList.add(t);
        }
        return tList;
    }

    @Override
    protected void dataSave(List<T> ts) {

    }
}
