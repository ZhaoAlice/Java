package algorithm.seqlist.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/6/24
 * @since 1.0.0
 */
public class BasicLinkedBag<T> implements Iterable<T> {
    private int n;
    private Node<T> first;

    public BasicLinkedBag() {
        this.n = 0;
        this.first = null;
    }

    /**
     * 列表是否为空
     * @return
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * 序列中元素的数量
     * @return
     */
    public int size() {
        return n;
    }

    /**
     * 头插法
     * @param item
     * @return
     */
    public void add(T item) {
        Node<T> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedIterator(first);
    }

    public class LinkedIterator implements Iterator<T> {
        private Node<T> current;

        public LinkedIterator(Node<T> first) {
            this.current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T currentItem = current.item;
            current = current.next;
            return currentItem;
        }
    }

    public static class Node<T> {
        private T item;
        private Node<T> next;

        public Node<T> getNext() {
            return next;
        }
    }
}