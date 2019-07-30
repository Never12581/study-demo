package template;

import java.util.List;

/**
 * @author huangbocai
 * @auther: bocai.huang
 * @create: 2019-05-20 11:17
 **/
public abstract class AbstractDataSynchronization<E,T> implements DataSynchronization<E> {
    @Override
    public final void dataSynchronized(List<E> e) {
        List<T> tList = dataTrans(e);

        dataSave(tList);
    }

    protected abstract List<T> dataTrans(List<E> e) ;


    protected abstract void dataSave(List<T> tList) ;
}
