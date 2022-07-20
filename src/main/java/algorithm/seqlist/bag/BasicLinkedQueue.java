package algorithm.seqlist.bag;

import java.util.Iterator;

import org.w3c.dom.Node;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/7/7
 * @since 1.0.0
 */
public class BasicLinkedQueue<T> implements Iterable<T> {
    private int n;
    private Node first;
    private Node last;

    public BasicLinkedQueue() {
        this.n = 0;
        this.first = null;
        this.last = null;
    }

    public boolean isEmpty() {
        return null != first;
    }

    public int size() {
        return n;
    }

    public void enqueue(T item) {
        Node<T> oldLast = last;
        last = new Node<T>();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }
        n++;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public static class Node<T> {
        private T item;
        private Node<T> next;
    }
}