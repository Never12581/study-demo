package responsibility_chain;

/**
 * @author: bocai.huang
 * @create: 2019-10-09 16:46
 **/
public class NodeChain {

    private Node first ;
    private Node end ;

    public void addNode(Node next ) {
        if( first == null ) {
            first = next;
            while (next.getNext() !=null) {
                next = next.getNext();
            }
            end = next;
            return;
        }
        end.setNext(next);
        end = next;
    }

    public Node getFirst() {
        return first;
    }

}
