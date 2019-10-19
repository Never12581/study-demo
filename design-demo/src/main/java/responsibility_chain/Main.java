package responsibility_chain;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: bocai.huang
 * @create: 2019-10-09 15:53
 **/
public class Main {

    public static void main(String[] args)
        throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Node node1 = new Node();
        node1.setNodeDesc("我是1号节点");
        Node node4 = new Node();
        node4.setNodeDesc("其实我是4号节点");
        node1.setNext(node4);
        Node node2 = new Node();
        node2.setNodeDesc("我是2号节点");
        Node node3 = new Node();
        node3.setNodeDesc("我是3号节点");
        NodeChain nodeChain = new NodeChain();
        nodeChain.addNode(node1);
        nodeChain.addNode(node2);
        nodeChain.addNode(node3);

        String result = nodeChain.execute();

        System.out.println(result);


        Class<Node> nodeClass = Node.class;
        String name = nodeClass.getName();
        System.out.println(name);


        String input = "responsibility_chain.Node#nodeDesc";

        Class a = Class.forName("responsibility_chain.Node");
        System.out.println(a == nodeClass);

        Field field = a.getDeclaredField("nodeDesc");

        Object o = a.newInstance();
        field.setAccessible(true);
        field.set(o,"哈哈哈哈，这是反射赋值 的");
        field.setAccessible(false);

        System.out.println(JSON.toJSONString(o));

        Map<String , Object> map = new HashMap<>();
        map.put("nodeDesc","这是map中反射赋值的");
        map.put("next",null);
        System.out.println(JSON.toJSONString(map));

    }
}
