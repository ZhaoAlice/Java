package algorithm.seqlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/7/7
 * @since 1.0.0
 */
public class BasicLinkedQueue<T> implements Iterable<T> {
    /** 队列大小 */
    private int n;
    private Node<T> first;
    private Node<T> last;

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

    /**
     * 只返回对头元素的值 元素并不出队
     * @return
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    /**
     * 入队
     * @param item
     */
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
        // 入队加1
        n++;
    }

    /**
     * 出队
     * @return
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T value = first.item;
        first = first.next;
        // 出队减去1
        n--;
        if (isEmpty()) {
            last = null;
        }
        return value;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedQueueIterator();
    }

    public class LinkedQueueIterator implements Iterator<T> {

        private Node<T> current = first;

        @Override
        public boolean hasNext() {
            return null != current;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T value = current.item;
            current = current.next;
            return value;
        }
    }

    public static class Node<T> {
        private T item;
        private Node<T> next;
    }
}