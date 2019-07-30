package template;

import java.util.List;

/**
 * @author huangbocai
 *
 * @param E 原数据类型
 * @param T 需要转化的数据类型
 */
public interface DataSynchronization<E> {

    /**
     *
     * @param e         原数据
     */
    void dataSynchronized(List<E> e);

}
