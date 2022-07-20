package algorithm.seqlist.bag;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * 〈〉<br>
 *
 * @author Carrie
 * @create 2022/6/27
 * @since 1.0.0
 */
public class BasicLinkedStack<T> implements Iterable<T>{
    private int n;
    private Node<T> first;

    public BasicLinkedStack() {
        n = 0;
        first = null;
    }

    public void push(T item) {
        Node<T> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> top = first;
        first = first.next;
        n--;
        return top.item;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    public boolean isEmpty() {
        return null == first;
    }

    public int size() {
        return n;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public static class LinkedIterator<T> implements Iterator<T> {

        private Node<T> current;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return null;
        }
    }

    public static class Node<T> {
        private T item;
        private Node<T> next;

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }
}