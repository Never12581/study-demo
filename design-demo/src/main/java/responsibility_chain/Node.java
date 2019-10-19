package responsibility_chain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: bocai.huang
 * @create: 2019-10-09 16:39
 **/
@Getter
@Setter
public class Node {

    /**
     * 源头数据格式：
     * <p> xml </p>
     * <p>json </p>
     */
    private String type;
    /**
     * 源头数据的 字段的类型
     */
    private String sourceFieldType;
    /**
     * 源头数据的 指定字段
     * <p> xml ： business.group.cgxh </p>
     * <p> json : com.hzx.ConfirmBook#cgxh </p>
     */
    private String sourceField;
    /**
     * 需要转化的数据的 类型
     */
    private String toFieldType;
    /**
     * 需要转化的 指定字段
     * <p> json : com.hzx.PurchasePlan#finacialId </p>
     * <p> xml : business.group.cgxh </p>
     */
    private String toField;

    private Node next;
    
}
